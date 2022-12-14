package com.zyj.paocai.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zyj.paocai.common.constant.Constant;
import com.zyj.paocai.common.entity.to.SkuPromotionTo;
import com.zyj.paocai.common.entity.vo.*;
import com.zyj.paocai.common.exception.BizCodeEnum;
import com.zyj.paocai.common.utils.PageUtils;
import com.zyj.paocai.common.utils.Query;
import com.zyj.paocai.common.utils.R;
import com.zyj.paocai.common.utils.RRException;
import com.zyj.paocai.product.dao.SkuInfoDao;
import com.zyj.paocai.product.entity.*;
import com.zyj.paocai.product.entity.vo.SkuItemVo;
import com.zyj.paocai.product.feign.CouponFeignService;
import com.zyj.paocai.product.feign.WareFeignService;
import com.zyj.paocai.product.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.stream.Collectors;


@Slf4j
@Service("skuInfoService")
public class SkuInfoServiceImpl extends ServiceImpl<SkuInfoDao, SkuInfoEntity> implements SkuInfoService {

    @Autowired
    SkuInfoDao skuInfoDao;

    @Autowired
    WareFeignService wareFeignService;

    @Autowired
    SkuImagesService skuImagesService;

    @Autowired
    SpuInfoDescService spuInfoDescService;

    @Autowired
    ProductAttrValueService productAttrValueService;

    @Autowired
    AttrGroupService attrGroupService;

    @Autowired
    SkuSaleAttrValueService skuSaleAttrValueService;

    @Autowired
    CategoryService categoryService;

    @Autowired
    CouponFeignService couponFeignService;

    @Autowired
    ThreadPoolExecutor executor;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SkuInfoEntity> page = this.page(
                new Query<SkuInfoEntity>().getPage(params),
                new QueryWrapper<SkuInfoEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public PageUtils queryPageByCondition(Map<String, Object> params) {
        /**
         * key: '??????',//???????????????
         * catelogId: 0,
         * brandId: 0,
         * min: 0,
         * max: 0
         */
        QueryWrapper<SkuInfoEntity> wrapper = new QueryWrapper<>();

        String key = (String) params.get("key");
        if (StringUtils.hasText(key)) {
            wrapper.and((w) -> {
                w.eq("sku_id", key).or().like("sku_name", key);
            });
        }

        String brandId = (String) params.get("brandId");
        if (StringUtils.hasText(brandId) && !"0".equalsIgnoreCase(brandId)) {
            wrapper.and((w) -> {
                w.eq("brand_id", brandId);
            });
        }

        String catelogId = (String) params.get("catelogId");
        if (StringUtils.hasText(catelogId) && !"0".equalsIgnoreCase(brandId)) {
            wrapper.eq("catalog_id", catelogId);
        }

        String min = (String) params.get("min");
        if (StringUtils.hasText(min)) {
            wrapper.ge("price", min);
        }

        String max = (String) params.get("max");
        if (StringUtils.hasText(max)) {
            try {
                BigDecimal bigDecimal = new BigDecimal(max);
                if (bigDecimal.compareTo(BigDecimal.ZERO) == 1) {
                    wrapper.le("price", max);
                }
            } catch (Exception e) {

            }
        }

        IPage<SkuInfoEntity> page = this.page(
                new Query<SkuInfoEntity>().getPage(params),
                wrapper
        );

        return new PageUtils(page);
    }

    @Override
    public List<SkuInfoEntity> getSkuBySpuId(Long spuId) {
        List<SkuInfoEntity> skus = baseMapper.selectList(new QueryWrapper<SkuInfoEntity>()
                .eq("spu_id", spuId));
        return skus;
    }

    @Override
    public Map<Long, String> getSkuNameInfos(List<Long> skuIds) {
        List<SkuInfoEntity> skuInfoEntities = listByIds(skuIds);
        Map<Long, String> map = skuInfoEntities.stream().collect(Collectors.toMap(SkuInfoEntity::getSkuId,
                SkuInfoEntity::getSkuName));
        return map;
    }

    /**
     * @param skuId
     * @return
     * @throws ExecutionException
     * @throws InterruptedException
     */
    @Override
    public SkuItemVo getItem(Long skuId) throws ExecutionException, InterruptedException {
        SkuItemVo vo = new SkuItemVo();
        // ??????sku????????????
        CompletableFuture<SkuDetailVo> getSkuInfoFuture = CompletableFuture.supplyAsync(() -> {
            SkuDetailVo skuDetail = getSkuDetail(skuId);
            vo.setSkuInfo(skuDetail);
            return skuDetail;
        }, executor);
        SkuDetailVo skuInfo = getSkuInfoFuture.get();
        Long spuId = skuInfo.getSpuId();
        Long catalogId = skuInfo.getCatalogId();

        // ??????sku???????????????
        CompletableFuture<Void> skuStockFuture = CompletableFuture.runAsync(() -> {
            R<SkuHasStockVo> r = wareFeignService.getSkuHasStock(skuId);
            if (!Constant.SUCCESS_CODE.equals(r.getCode())) {
                throw new RRException("????????????sku??????????????????," + r.getMsg(), r.getCode());
            }
            SkuHasStockVo skuHasStockVo = r.getData();
            vo.setHasStock(skuHasStockVo.getHasStock());
        }, executor);

        // ??????sku????????????
        CompletableFuture<Void> skuImagesFuture = CompletableFuture.runAsync(() -> {
            List<SkuImagesEntity> images = skuImagesService.getImagesBySkuId(skuId);
            if (CollectionUtils.isEmpty(images)) {
                log.warn("????????????????????????????????????skuId:" + skuId);
            }
            vo.setImages(images);
        }, executor);

        // ??????spu????????????
        CompletableFuture<Void> descFuture = CompletableFuture.runAsync(() -> {
            SpuInfoDescEntity desc = spuInfoDescService.getDescBySpuId(spuId);
            if (desc == null) {
                log.warn("????????????????????????????????????skuId:" + skuId);
            }
            String[] split = desc.getDecript().split(",");
            if (split != null && split.length > 0) {
                vo.setDescImages(Arrays.asList(split));
            }
        }, executor);

        // ??????spu??????????????????
        CompletableFuture<Void> groupAttrsFuture = CompletableFuture.runAsync(() -> {
            List<SkuItemVo.SpuItemAttrGroupVo> groupAttrs = attrGroupService.getAttrGroupWithAttrsBySpuId(spuId, catalogId);
            if (CollectionUtils.isEmpty(groupAttrs)) {
                log.warn("??????????????????????????????????????????spuId:" + spuId);
            }
            vo.setGroupAttrs(groupAttrs);
        }, executor);

        // ????????????????????????
        CompletableFuture<Void> introduceParametersFuture = CompletableFuture.runAsync(() -> {
            List<ProductAttrValueEntity> attrValues = productAttrValueService.getQuickShowAttrBySpuId(spuId);
            if (!CollectionUtils.isEmpty(attrValues)) {
                List<SkuItemVo.SpuBaseAttrVo> introduceParameters = attrValues.stream().map((attr) -> {
                    SkuItemVo.SpuBaseAttrVo spuBaseAttrVo = new SkuItemVo.SpuBaseAttrVo();
                    spuBaseAttrVo.setAttrName(attr.getAttrName());
                    spuBaseAttrVo.setAttrValue(attr.getAttrValue());
                    return spuBaseAttrVo;
                }).collect(Collectors.toList());
                vo.setIntroduceParameters(introduceParameters);
            }
        }, executor);

        // ??????spu??????????????????
        CompletableFuture<Void> saleAttrsFuture = CompletableFuture.runAsync(() -> {
            List<SkuItemVo.SkuItemSaleAttrVo> saleAttrs = skuSaleAttrValueService.getSaleAttrsBySpuId(spuId);
            if (CollectionUtils.isEmpty(saleAttrs)) {
                log.info("??????????????????????????????????????????spuId:" + spuId);
            }
            vo.setSaleAttrs(saleAttrs);
        }, executor);

        // ??????sku????????????
        CompletableFuture<Void> skuBoundsFuture = CompletableFuture.runAsync(() -> {
            R<SkuBoundsVo> r = couponFeignService.getBoundsBySkuId(skuId);
            if (!Constant.SUCCESS_CODE.equals(r.getCode())) {
                throw new RRException("??????sku??????????????????,skuId:" + skuId, r.getCode());
            }
            vo.setBounds(r.getData());
        }, executor);

        CompletableFuture.allOf(skuStockFuture, skuImagesFuture, descFuture, groupAttrsFuture,
                saleAttrsFuture, skuBoundsFuture, introduceParametersFuture).get();
        return vo;
    }

    /**
     * ??????sku????????????
     *
     * @param skuId
     * @return
     */
    @Override
    public SkuDetailVo getSkuDetail(Long skuId) {
        SkuDetailVo detail = skuInfoDao.getSkuDetail(skuId);
        if (detail == null) {
            throw new RRException("??????????????????????????????,skuId:" + skuId, BizCodeEnum.PRODUCT_NO_EXIST_EXCEPTION.getCode());
        }
        if (detail.getCatalogId() == null || detail.getCatalogId() < 0) {
            throw new RRException("????????????????????????????????????", BizCodeEnum.PRODUCT_CATEGORY_NO_EXIST_EXCEPTION.getCode());
        }
        List<CatalogBaseVo> catalogPath = categoryService.getCatalogBaseVoPath(detail.getCatalogId());
        detail.setCatalogPath(catalogPath);
        return detail;
    }

    /**
     * ???????????????????????????????????????
     *
     * @param pageSize
     * @param page
     * @return
     */
    @Override
    public List<SkuInfoEntity> getHotSales(Integer pageSize, Integer page) {
        // TODO ??????????????????????????????????????????
        // ??????????????????????????????
        List<SkuInfoEntity> hotSales = baseMapper.getHotSales(pageSize, (page - 1) * pageSize);
        return hotSales;
    }

    /**
     * ??????????????????????????????
     *
     * @param skuId
     * @return
     */
    @Override
    public CartSkuItem getCartSkuItem(Long skuId) throws ExecutionException, InterruptedException {
        // ??????sku????????????
        CartSkuItem skuItem = skuInfoDao.getCartSkuInfoBySkuId(skuId);
        if (skuItem == null) {
            throw new RRException("?????????????????????,skuId:" + skuId, BizCodeEnum.PRODUCT_NO_EXIST_EXCEPTION.getCode());
        }
        // ?????????
        skuItem.setPrice(skuItem.getOriginalPrice());

        // ??????sku????????????
        List<SkuSaleAttrValueEntity> saleAttrs = skuSaleAttrValueService.getSaleAttrsBySkuId(skuId);
        if (!CollectionUtils.isEmpty(saleAttrs)) {
            List<AttrBaseVo> attrs = saleAttrs.stream().map(saleAttr -> {
                AttrBaseVo attr = new AttrBaseVo();
                attr.setAttrId(saleAttr.getAttrId());
                attr.setAttrName(saleAttr.getAttrName());
                attr.setAttrValue(saleAttr.getAttrValue());
                return attr;
            }).collect(Collectors.toList());
            skuItem.setAttrs(attrs);
        }
        // ??????????????????
        R<SkuPromotionTo> r = couponFeignService.getSkuPromotion(skuId);
        if (!Constant.SUCCESS_CODE.equals(r.getCode())) {
            throw new RRException(r.getMsg(), r.getCode());
        }
        SkuPromotionTo skuPromotionTo = r.getData();
        if (skuPromotionTo != null) {
            skuItem.setFullReductions(skuPromotionTo.getReductions());
            skuItem.setLadders(skuPromotionTo.getLadders());
            if (skuPromotionTo.getBounds() != null) {
                skuItem.setGrowth(skuPromotionTo.getBounds().getGrowBounds());
                skuItem.setIntegration(skuPromotionTo.getBounds().getBuyBounds());
            } else {
                skuItem.setGrowth(new BigDecimal(0));
                skuItem.setIntegration(new BigDecimal(0));
            }
        }
        return skuItem;
    }

    /**
     * ??????????????????????????????
     *
     * @param skuIds
     * @return
     */
    @Override
    public List<CartSkuItem> getSkuItems(List<Long> skuIds) {
        if (skuIds.size() == 0) {
            throw new RRException("??????????????????????????????", BizCodeEnum.PRODUCT_SERVICE_EXCEPTION.getCode());
        }
        // ?????????????????????sku????????????
        List<CartSkuItem> skuItems = skuInfoDao.getCartSkuInfoBatchIds(skuIds);
        if (skuItems == null || skuItems.size() == 0 || skuItems.size() != skuIds.size()) {
            throw new RRException(BizCodeEnum.PRODUCT_NO_EXIST_EXCEPTION.getMsg(), BizCodeEnum.PRODUCT_NO_EXIST_EXCEPTION.getCode());
        }
        for (CartSkuItem skuItem : skuItems) {
            Long skuId = skuItem.getSkuId();
            skuItem.setPrice(skuItem.getOriginalPrice());
            // sku??????????????????
            List<SkuSaleAttrValueEntity> saleAttrs = skuSaleAttrValueService.getSaleAttrsBySkuId(skuId);
            if (!CollectionUtils.isEmpty(saleAttrs)) {
                List<AttrBaseVo> attrs = saleAttrs.stream().map(saleAttr -> {
                    AttrBaseVo attr = new AttrBaseVo();
                    attr.setAttrId(saleAttr.getAttrId());
                    attr.setAttrName(saleAttr.getAttrName());
                    attr.setAttrValue(saleAttr.getAttrValue());
                    return attr;
                }).collect(Collectors.toList());
                skuItem.setAttrs(attrs);
            }
            // ??????????????????
            R<SkuPromotionTo> r = couponFeignService.getSkuPromotion(skuId);
            if (!Constant.SUCCESS_CODE.equals(r.getCode())) {
                throw new RRException(r.getMsg(), r.getCode());
            }
            SkuPromotionTo skuPromotionTo = r.getData();
            if (skuPromotionTo != null) {
                skuItem.setFullReductions(skuPromotionTo.getReductions());
                skuItem.setLadders(skuPromotionTo.getLadders());
                if (skuPromotionTo.getBounds() != null) {
                    skuItem.setGrowth(skuPromotionTo.getBounds().getGrowBounds());
                    skuItem.setIntegration(skuPromotionTo.getBounds().getBuyBounds());
                } else {
                    skuItem.setGrowth(new BigDecimal(0));
                    skuItem.setIntegration(new BigDecimal(0));
                }
            }
        }
        return skuItems;
    }
}