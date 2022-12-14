package com.zyj.paocai.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zyj.paocai.common.constant.Constant;
import com.zyj.paocai.common.constant.ProductConstant;
import com.zyj.paocai.common.entity.to.SkuReductionTo;
import com.zyj.paocai.common.entity.to.SpuBoundsTo;
import com.zyj.paocai.common.entity.to.es.SkuEsModel;
import com.zyj.paocai.common.entity.vo.SkuHasStockVo;
import com.zyj.paocai.common.exception.BizCodeEnum;
import com.zyj.paocai.common.utils.PageUtils;
import com.zyj.paocai.common.utils.Query;
import com.zyj.paocai.common.utils.R;
import com.zyj.paocai.common.utils.RRException;
import com.zyj.paocai.product.dao.SpuInfoDao;
import com.zyj.paocai.product.entity.*;
import com.zyj.paocai.product.entity.vo.*;
import com.zyj.paocai.product.feign.CouponFeignService;
import com.zyj.paocai.product.feign.SearchFeignService;
import com.zyj.paocai.product.feign.WareFeignService;
import com.zyj.paocai.product.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.stream.Collectors;

@Slf4j
@Service("spuInfoService")
public class SpuInfoServiceImpl extends ServiceImpl<SpuInfoDao, SpuInfoEntity> implements SpuInfoService {

    @Autowired
    SpuInfoDao spuInfoDao;

    @Autowired
    SpuImagesService spuImagesService;

    @Autowired
    SpuInfoDescService spuInfoDescService;

    @Autowired
    ProductAttrValueService productAttrValueService;

    @Autowired
    AttrService attrService;

    @Autowired
    CouponFeignService couponFeignService;

    @Autowired
    SkuInfoService skuInfoService;

    @Autowired
    SkuImagesService skuImagesService;

    @Autowired
    SkuSaleAttrValueService skuSaleAttrValueService;

    @Autowired
    BrandService brandService;

    @Autowired
    SpuCommentService spuCommentService;

    @Autowired
    SearchFeignService searchFeignService;

    @Autowired
    CategoryService categoryService;

    @Autowired
    WareFeignService wareFeignService;

    @Autowired
    ThreadPoolExecutor executor;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
//        status=0&key=&brandId=1&catelogId=1349&page=1&limit=10
        QueryWrapper<SpuInfoEntity> wrapper = new QueryWrapper<>();

        String key = (String) params.get("key");
        if (StringUtils.hasText(key)) {
            wrapper.and((w) -> {
                w.eq("id", key).or().like("spu_name", key);
            });
        }

        String status = (String) params.get("status");
        if (StringUtils.hasText(status)) {
            wrapper.eq("publish_status", Integer.parseInt(status));
        }

        String catelogId = (String) params.get("catelogId");
        if (StringUtils.hasText(catelogId)) {
            Long id = Long.parseLong(catelogId);
            if (id > 0) {
                wrapper.eq("catalog_id", id);
            }
        }

        String brandId = (String) params.get("brandId");
        if (StringUtils.hasText(brandId)) {
            Long id = Long.parseLong(brandId);
            if (id > 0) {
                wrapper.eq("brand_id", brandId);
            }
        }


        IPage<SpuInfoEntity> page = this.page(
                new Query<SpuInfoEntity>().getPage(params),
                wrapper
        );

        return new PageUtils(page);
    }

    /**
     * ????????????
     *
     * @param vo
     */
    @Transactional
    @Override
    public void saveSpuInfo(SpuSaveVo vo) {
        // 1?????????spu??????
        SpuInfoEntity spuInfoEntity = saveSpu(vo);
        Long spuId = spuInfoEntity.getId();

        // 2?????????spu??????(??????)
        List<String> decript = vo.getDecript();
        SpuInfoDescEntity descEntity = new SpuInfoDescEntity();
        // spuId?????????????????????????????????????????????????????????????????????spuInfoEntity???????????????????????????????????????spuId
        descEntity.setSpuId(spuId);
        // ??????,??????????????????
        descEntity.setDecript(String.join(",", decript));
        spuInfoDescService.save(descEntity);

        // 3?????????spu??????
        CompletableFuture<Void> saveImagesFuture = CompletableFuture.runAsync(() -> {
            List<String> images = vo.getImages();
            spuImagesService.saveImages(spuId, images);
        }, executor);

        // 4?????????spu????????????
        CompletableFuture<Void> saveSpuBaseAttrsFuture = CompletableFuture.runAsync(() -> {
            saveSpuBaseAttrs(vo, spuId);
        }, executor);

        // 5?????????spu????????????
        CompletableFuture<Void> saveSpuBoundsFuture = CompletableFuture.runAsync(() -> {
            saveSpuBounds(vo, spuId);
        }, executor);

        // 6?????????sku??????
        List<Skus> skus = vo.getSkus();
        if (skus == null || skus.size() == 0) {
            throw new RRException(BizCodeEnum.PRODUCT_NO_EXIST_EXCEPTION.getMsg(),BizCodeEnum.PRODUCT_NO_EXIST_EXCEPTION.getCode());
        }
        CompletableFuture<Void> saveSkuInfoFuture = CompletableFuture.runAsync(() -> {
            saveSkuInfo(spuInfoEntity, skus);
        }, executor);

        try {
            CompletableFuture.allOf(saveImagesFuture, saveSkuInfoFuture, saveSpuBoundsFuture,
                    saveSpuBaseAttrsFuture).get();
        } catch (Exception e) {
            throw new RRException(e.getMessage(),BizCodeEnum.PRODUCT_SERVICE_EXCEPTION.getCode());
        }
    }

    /**
     * ????????????
     *
     * @param spuId
     */
    @Transactional
    @Override
    public void up(Long spuId) {
        // 1???????????????spu???????????????sku????????????????????????
        List<SkuInfoEntity> skus = skuInfoService.getSkuBySpuId(spuId);
        if (skus == null || skus.size() == 0) {
            throw new RRException("?????????spu_id:" + spuId + "?????????sku????????????",BizCodeEnum.PRODUCT_NO_EXIST_EXCEPTION.getCode());
        }
        List<Long> skuIds = skus.stream().map(SkuInfoEntity::getSkuId).collect(Collectors.toList());
        // ??????????????????
        BrandEntity brand = brandService.getBrandBySpuId(spuId);
        // ??????????????????
        CategoryEntity category = categoryService.getCategoryBySpuId(spuId);


        // ????????????sku??????????????????????????????spu???????????????????????????????????????????????????????????????
        List<SkuEsModel.Attrs> attrsList = getAttrs(spuId);

        // 3???????????????????????????????????????????????????
        Map<Long, Boolean> skuStockMap = null;
        try {
            R<List<SkuHasStockVo>> r = wareFeignService.getSkuHasStockBatch(skuIds);
            skuStockMap = r.getData().stream().collect(Collectors.toMap(SkuHasStockVo::getSkuId, SkuHasStockVo::getHasStock));
        } catch (Exception e) {
            log.error("????????????????????????:??????{}", e);
            throw new RRException(e.getMessage(),BizCodeEnum.PRODUCT_SERVICE_EXCEPTION.getCode());
        }

        // 2???????????????sku??????
        Map<Long, Boolean> finalSkuStockMap = skuStockMap;
        List<SkuEsModel> upProducts = skus.stream().map(sku -> {
            SkuEsModel esModel = new SkuEsModel();
            BeanUtils.copyProperties(sku, esModel);
            esModel.setSkuPrice(sku.getPrice());
            esModel.setSkuImg(sku.getSkuDefaultImg());
            esModel.setCatalogId(sku.getCatalogId());

            Long skuId = sku.getSkuId();
            // ???????????????
            if (finalSkuStockMap != null) {
                esModel.setHasStock(finalSkuStockMap.get(skuId));
            } else {
                esModel.setHasStock(false);
            }
            // ?????????????????????0
            esModel.setHotScore(0L);


            // 4?????????????????????????????????
            esModel.setBrandName(brand.getName());
            esModel.setBrandImg(brand.getLogo());
            esModel.setCatalogName(category.getName());

            // ??????????????????
            esModel.setAttrs(attrsList);

            return esModel;
        }).collect(Collectors.toList());

        // 5?????????????????????es???????????????paocai-search
        R r = searchFeignService.productStatusUp(upProducts);
        if (Constant.SUCCESS_CODE.equals(r.getCode())) {
            // ??????????????????,??????spu????????????
            baseMapper.updateSpuStatus(spuId, ProductConstant.StatusEnum.SPU_NEW.getCode());
        } else {
            // ??????????????????
            // TODO ??????????????????????????????????,????????????
            /*
             * Feign????????????
             * */
        }

    }

    /**
     * ??????spu?????????????????????
     *
     * @param spuId
     * @return
     */
    private List<SkuEsModel.Attrs> getAttrs(Long spuId) {
        List<SkuEsModel.Attrs> attrs = productAttrValueService.getSearchAttrs(spuId);
        if (attrs == null || attrs.size() == 0) {
            log.info("spu:{}????????????????????????", spuId);
        }
        return attrs;
    }

    /**
     * ??????sku??????
     *
     * @param spuInfoEntity
     * @param skus
     */
    @Transactional
    public void saveSkuInfo(SpuInfoEntity spuInfoEntity, List<Skus> skus) {
        skus.forEach(item -> {
            String defaultImg = "";
            for (Images image : item.getImages()) {
                if (image.getDefaultImg() == 1) {
                    defaultImg = image.getImgUrl();
                }
            }
            SkuInfoEntity skuInfoEntity = new SkuInfoEntity();
            BeanUtils.copyProperties(item, skuInfoEntity);
            skuInfoEntity.setBrandId(spuInfoEntity.getBrandId());
            skuInfoEntity.setCatalogId(spuInfoEntity.getCatalogId());
            skuInfoEntity.setSaleCount(0L);
            skuInfoEntity.setSpuId(spuInfoEntity.getId());
            skuInfoEntity.setSkuDefaultImg(defaultImg);
            skuInfoService.save(skuInfoEntity);

            Long skuId = skuInfoEntity.getSkuId();
            // 6.2???sku??????????????????pms_sku_images
            List<SkuImagesEntity> imagesEntities = item.getImages().stream().map(img -> {
                SkuImagesEntity skuImagesEntity = new SkuImagesEntity();
                skuImagesEntity.setSkuId(skuId);
                skuImagesEntity.setImgUrl(img.getImgUrl());
                skuImagesEntity.setDefaultImg(img.getDefaultImg());
                return skuImagesEntity;
            }).filter(entity -> {
                return StringUtils.hasText(entity.getImgUrl());
            }).collect(Collectors.toList());
            // TODO ???????????????????????????
            if (!CollectionUtils.isEmpty(imagesEntities)) {
                skuImagesService.saveBatch(imagesEntities);
            }

            // 6.3???sku??????????????????pms_sku_sale_attr_value
            List<Attr> attr = item.getAttr();
            List<SkuSaleAttrValueEntity> skuSaleAttrValueEntities = attr.stream().map(a -> {
                SkuSaleAttrValueEntity skuSaleAttrValueEntity = new SkuSaleAttrValueEntity();
                BeanUtils.copyProperties(a, skuSaleAttrValueEntity);
                skuSaleAttrValueEntity.setSkuId(skuId);
                return skuSaleAttrValueEntity;
            }).collect(Collectors.toList());
            if (!CollectionUtils.isEmpty(skuSaleAttrValueEntities)) {
                skuSaleAttrValueService.saveBatch(skuSaleAttrValueEntities);
            }

            // 6.4???sku???????????????????????????paocai-sms????????????sms_sku_ladder???sms_sku_full_reduction???sms_member_price
            SkuReductionTo skuReductionTo = new SkuReductionTo();
            BeanUtils.copyProperties(item, skuReductionTo);
            skuReductionTo.setSkuId(spuInfoEntity.getId());
            if (skuReductionTo.getFullCount() > 0 || skuReductionTo.getFullPrice().compareTo(BigDecimal.ZERO) == 1) {
                R r1 = couponFeignService.saveSkuReduction(skuReductionTo);
                if (r1.getCode() != 0) {
                    log.error("????????????sku??????????????????");
                }
            }
        });
    }

    /**
     * ??????spu????????????
     *
     * @param vo
     * @param spuId
     */
    @Transactional
    public void saveSpuBounds(SpuSaveVo vo, Long spuId) {
        Bounds bounds = vo.getBounds();
        SpuBoundsTo spuBoundsTo = new SpuBoundsTo();
        BeanUtils.copyProperties(bounds, spuBoundsTo);
        spuBoundsTo.setSpuId(spuId);
        R r = couponFeignService.saveSpuBounds(spuBoundsTo);
        if (!Constant.SUCCESS_CODE.equals(r.getCode())) {
            throw new RRException(r.getMsg(),r.getCode());
        }
    }

    /**
     * ??????spu????????????
     *
     * @param vo
     * @param spuId
     */
    private void saveSpuBaseAttrs(SpuSaveVo vo, Long spuId) {
        List<BaseAttrs> baseAttrs = vo.getBaseAttrs();
        if (baseAttrs != null && baseAttrs.size() > 0) {
            List<ProductAttrValueEntity> attrValueList = baseAttrs.stream().map(attr -> {
                ProductAttrValueEntity entity = new ProductAttrValueEntity();
                entity.setSpuId(spuId);
                entity.setAttrId(attr.getAttrId());
                AttrEntity attrEntity = attrService.getById(attr.getAttrId());
                entity.setAttrName(attrEntity.getAttrName());
                entity.setAttrValue(attr.getAttrValues());
                entity.setQuickShow(attr.getShowDesc());
                return entity;
            }).collect(Collectors.toList());
            productAttrValueService.saveBatch(attrValueList);
        }
    }

    /**
     * ??????spu??????
     *
     * @param vo
     * @return
     */
    @Transactional
    public SpuInfoEntity saveSpu(SpuSaveVo vo) {
        SpuInfoEntity spuInfoEntity = new SpuInfoEntity();
        spuInfoEntity.setSpuName(vo.getSpuName());
        spuInfoEntity.setSpuDescription(vo.getSpuDescription());
        spuInfoEntity.setWeight(vo.getWeight());
        spuInfoEntity.setCatalogId(vo.getCatalogId());
        spuInfoEntity.setCreateTime(new Date());
        spuInfoEntity.setUpdateTime(new Date());
        spuInfoEntity.setBrandId(vo.getBrandId());
        spuInfoEntity.setPublishStatus(vo.getPublishStatus());
        this.save(spuInfoEntity);
        return spuInfoEntity;
    }

}