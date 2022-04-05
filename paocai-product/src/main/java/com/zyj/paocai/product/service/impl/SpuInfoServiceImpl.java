package com.zyj.paocai.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zyj.paocai.common.constant.Constant;
import com.zyj.paocai.common.constant.ProductConstant;
import com.zyj.paocai.common.entity.to.SkuHasStockVo;
import com.zyj.paocai.common.entity.to.SkuReductionTo;
import com.zyj.paocai.common.entity.to.SpuBoundsTo;
import com.zyj.paocai.common.entity.to.es.SkuEsModel;
import com.zyj.paocai.common.utils.PageUtils;
import com.zyj.paocai.common.utils.Query;
import com.zyj.paocai.common.utils.R;
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
import java.util.*;
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
     * 新增商品
     *
     * @param vo
     */
    @Transactional
    @Override
    public void saveSpuInfo(SpuSaveVo vo) {
        // 1、保存spu信息
        SpuInfoEntity spuInfoEntity = saveSpu(vo);
        Long spuId = spuInfoEntity.getId();

        // 2、保存spu描述(图片)
        List<String> decript = vo.getDecript();
        SpuInfoDescEntity descEntity = new SpuInfoDescEntity();
        // spuId因为是自增主键会在上面保存基本数据后，又赋值给spuInfoEntity对象，所以这里拿得到生成的spuId
        descEntity.setSpuId(spuId);
        // 使用,隔开进行拼接
        descEntity.setDecript(String.join(",", decript));
        spuInfoDescService.save(descEntity);

        // 3、保存spu图片
        CompletableFuture<Void> saveImagesFuture = CompletableFuture.runAsync(() -> {
            List<String> images = vo.getImages();
            spuImagesService.saveImages(spuId, images);
        }, executor);

        // 4、保存spu基本参数
        CompletableFuture<Void> saveSpuBaseAttrsFuture = CompletableFuture.runAsync(() -> {
            saveSpuBaseAttrs(vo, spuId);
        }, executor);

        // 5、保存spu积分信息
        CompletableFuture<Void> saveSpuBoundsFuture = CompletableFuture.runAsync(() -> {
            saveSpuBounds(vo, spuId);
        }, executor);

        // 6、保存sku信息
        List<Skus> skus = vo.getSkus();
        if (skus == null || skus.size() == 0) {
            throw new RuntimeException("缺少sku商品信息");
        }
        CompletableFuture<Void> saveSkuInfoFuture = CompletableFuture.runAsync(() -> {
            saveSkuInfo(spuInfoEntity, skus);
        }, executor);

        try {
            CompletableFuture.allOf(saveImagesFuture, saveSkuInfoFuture, saveSpuBoundsFuture,
                    saveSpuBaseAttrsFuture).get();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 上架商品
     *
     * @param spuId
     */
    @Transactional
    @Override
    public void up(Long spuId) {
        // 1、查出当前spu对应的所有sku信息，品牌名字等
        List<SkuInfoEntity> skus = skuInfoService.getSkuBySpuId(spuId);
        if (skus == null || skus.size() == 0) {
            throw new RuntimeException("不存在spu_id:" + spuId + "对应的sku商品信息");
        }
        List<Long> skuIds = skus.stream().map(SkuInfoEntity::getSkuId).collect(Collectors.toList());

        // 查询当前sku的所有规格属性，同个spu下的规格属性是一样的，所以只需要查一遍即可
        List<SkuEsModel.Attrs> attrsList = getAttrs(spuId);

        // 3、发送远程调用，库存系统是否有库存
        Map<Long, Boolean> skuStockMap = null;
        try{
            R<List<SkuHasStockVo>> r = wareFeignService.getSkuHasStockBatch(skuIds);
            skuStockMap = r.getData().stream().collect(Collectors.toMap(SkuHasStockVo::getSkuId,SkuHasStockVo::getHasStock));
        }catch (Exception e){
            log.error("库存服务查询异常:原因{}",e);
            throw new RuntimeException(e);
        }

        // 2、封装每个sku信息
        Map<Long, Boolean> finalSkuStockMap = skuStockMap;
        List<SkuEsModel> upProducts = skus.stream().map(sku -> {
            SkuEsModel esModel = new SkuEsModel();
            BeanUtils.copyProperties(sku, esModel);
            esModel.setSkuPrice(sku.getPrice());
            esModel.setSkuImg(sku.getSkuDefaultImg());
            esModel.setCatalogId(sku.getCatalogId());

            // 是否有库存
            if(finalSkuStockMap != null){
                esModel.setHasStock(finalSkuStockMap.get(sku.getSkuId()));
            }else{
                esModel.setHasStock(false);
            }
            // 热度评分，默认0
            esModel.setHotScore(0L);

            // 4、查询品牌和分类的名字信息
            BrandEntity brand = brandService.getById(sku.getBrandId());
            esModel.setBrandName(brand.getName());
            CategoryEntity category = categoryService.getById(sku.getCatalogId());
            esModel.setCatalogName(category.getName());

            // 设置检索属性
            esModel.setAttrs(attrsList);

            return esModel;
        }).collect(Collectors.toList());

        // 5、将数据发送给es进行保存，paocai-search
        R r = searchFeignService.productStatusUp(upProducts);
        if(Constant.SUCCESS_CODE.equals(r.getCode())){
            // 远程调用成功,修改spu上架状态
            baseMapper.updateSpuStatus(spuId, ProductConstant.StatusEnum.SPU_NEW.getCode());
        }else{
            // 远程调用失败
            // TODO 重复调用问题?接口幂等性,重试机制
            /*
             * Feign调用流程
             * */
        }

    }

    private List<SkuEsModel.Attrs> getAttrs(Long spuId) {
        List<ProductAttrValueEntity> baseAttr = productAttrValueService.baseAttrListForSpu(spuId);
        List<Long> attrIds = baseAttr.stream().map(attr -> {
            return attr.getAttrId();
        }).collect(Collectors.toList());
        List<Long> searchAttrIds = attrService.selectSearchAttrIds(attrIds);
        Set<Long> idSet = new HashSet<>(searchAttrIds);
        List<SkuEsModel.Attrs> attrsList = baseAttr.stream().filter(item -> {
            return idSet.contains(item.getAttrId());
        }).map(item -> {
            SkuEsModel.Attrs attrs1 = new SkuEsModel.Attrs();
            BeanUtils.copyProperties(item, attrs1);
            return attrs1;
        }).collect(Collectors.toList());
        return attrsList;
    }

    /**
     * 保存sku信息
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
            // 6.2、sku的图片信息，pms_sku_images
            List<SkuImagesEntity> imagesEntities = item.getImages().stream().map(img -> {
                SkuImagesEntity skuImagesEntity = new SkuImagesEntity();
                skuImagesEntity.setSkuId(skuId);
                skuImagesEntity.setImgUrl(img.getImgUrl());
                skuImagesEntity.setDefaultImg(img.getDefaultImg());
                return skuImagesEntity;
            }).filter(entity -> {
                return StringUtils.hasText(entity.getImgUrl());
            }).collect(Collectors.toList());
            // TODO 没有路径的无需保存
            if (!CollectionUtils.isEmpty(imagesEntities)) {
                skuImagesService.saveBatch(imagesEntities);
            }

            // 6.3、sku的销售属性，pms_sku_sale_attr_value
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

            // 6.4、sku的优惠、满减信息，paocai-sms数据库中sms_sku_ladder、sms_sku_full_reduction、sms_member_price
            SkuReductionTo skuReductionTo = new SkuReductionTo();
            BeanUtils.copyProperties(item, skuReductionTo);
            skuReductionTo.setSkuId(spuInfoEntity.getId());
            if (skuReductionTo.getFullCount() > 0 || skuReductionTo.getFullPrice().compareTo(BigDecimal.ZERO) == 1) {
                R r1 = couponFeignService.saveSkuReduction(skuReductionTo);
                if (r1.getCode() != 0) {
                    log.error("远程保存sku优惠信息失败");
                }
            }
        });
    }

    /**
     * 保存spu积分信息
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
            log.error("远程保存spu的积分信息失败");
            throw new RuntimeException(r.getMsg());
        }
    }

    /**
     * 保存spu基本参数
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
     * 保存spu信息
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