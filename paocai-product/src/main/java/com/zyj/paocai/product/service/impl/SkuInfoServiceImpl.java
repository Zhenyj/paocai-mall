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
         * key: '华为',//检索关键字
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
        // 获取sku基本信息
        CompletableFuture<SkuDetailVo> getSkuInfoFuture = CompletableFuture.supplyAsync(() -> {
            SkuDetailVo skuDetail = getSkuDetail(skuId);
            vo.setSkuInfo(skuDetail);
            return skuDetail;
        }, executor);
        SkuDetailVo skuInfo = getSkuInfoFuture.get();
        Long spuId = skuInfo.getSpuId();
        Long catalogId = skuInfo.getCatalogId();

        // 获取sku是否有库存
        CompletableFuture<Void> skuStockFuture = CompletableFuture.runAsync(() -> {
            R<SkuHasStockVo> r = wareFeignService.getSkuHasStock(skuId);
            if (!Constant.SUCCESS_CODE.equals(r.getCode())) {
                throw new RRException("远程获取sku库存信息失败," + r.getMsg(), r.getCode());
            }
            SkuHasStockVo skuHasStockVo = r.getData();
            vo.setHasStock(skuHasStockVo.getHasStock());
        }, executor);

        // 获取sku图片信息
        CompletableFuture<Void> skuImagesFuture = CompletableFuture.runAsync(() -> {
            List<SkuImagesEntity> images = skuImagesService.getImagesBySkuId(skuId);
            if (CollectionUtils.isEmpty(images)) {
                log.warn("没有相关商品的图片信息，skuId:" + skuId);
            }
            vo.setImages(images);
        }, executor);

        // 获取spu描述信息
        CompletableFuture<Void> descFuture = CompletableFuture.runAsync(() -> {
            SpuInfoDescEntity desc = spuInfoDescService.getDescBySpuId(spuId);
            if (desc == null) {
                log.warn("没有相关商品的描述信息，skuId:" + skuId);
            }
            String[] split = desc.getDecript().split(",");
            if (split != null && split.length > 0) {
                vo.setDescImages(Arrays.asList(split));
            }
        }, executor);

        // 获取spu规格参数信息
        CompletableFuture<Void> groupAttrsFuture = CompletableFuture.runAsync(() -> {
            List<SkuItemVo.SpuItemAttrGroupVo> groupAttrs = attrGroupService.getAttrGroupWithAttrsBySpuId(spuId, catalogId);
            if (CollectionUtils.isEmpty(groupAttrs)) {
                log.warn("没有相关商品的规格参数信息，spuId:" + spuId);
            }
            vo.setGroupAttrs(groupAttrs);
        }, executor);

        // 获取商品介绍参数
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

        // 获取spu销售属性组合
        CompletableFuture<Void> saleAttrsFuture = CompletableFuture.runAsync(() -> {
            List<SkuItemVo.SkuItemSaleAttrVo> saleAttrs = skuSaleAttrValueService.getSaleAttrsBySpuId(spuId);
            if (CollectionUtils.isEmpty(saleAttrs)) {
                log.info("没有相关商品的销售属性组合，spuId:" + spuId);
            }
            vo.setSaleAttrs(saleAttrs);
        }, executor);

        // 获取sku积分信息
        CompletableFuture<Void> skuBoundsFuture = CompletableFuture.runAsync(() -> {
            R<SkuBoundsVo> r = couponFeignService.getBoundsBySkuId(skuId);
            if (!Constant.SUCCESS_CODE.equals(r.getCode())) {
                throw new RRException("获取sku积分信息失败,skuId:" + skuId, r.getCode());
            }
            vo.setBounds(r.getData());
        }, executor);

        CompletableFuture.allOf(skuStockFuture, skuImagesFuture, descFuture, groupAttrsFuture,
                saleAttrsFuture, skuBoundsFuture, introduceParametersFuture).get();
        return vo;
    }

    /**
     * 获取sku详细信息
     *
     * @param skuId
     * @return
     */
    @Override
    public SkuDetailVo getSkuDetail(Long skuId) {
        SkuDetailVo detail = skuInfoDao.getSkuDetail(skuId);
        if (detail == null) {
            throw new RRException("获取商品详细信息失败,skuId:" + skuId, BizCodeEnum.PRODUCT_NO_EXIST_EXCEPTION.getCode());
        }
        if (detail.getCatalogId() == null || detail.getCatalogId() < 0) {
            throw new RRException("商品分类信息异常或不存在", BizCodeEnum.PRODUCT_CATEGORY_NO_EXIST_EXCEPTION.getCode());
        }
        List<CatalogBaseVo> catalogPath = categoryService.getCatalogBaseVoPath(detail.getCatalogId());
        detail.setCatalogPath(catalogPath);
        return detail;
    }

    /**
     * 获取猜你喜欢、热销商品数据
     *
     * @param pageSize
     * @param page
     * @return
     */
    @Override
    public List<SkuInfoEntity> getHotSales(Integer pageSize, Integer page) {
        // TODO 分析用户最近搜索或购买的商品
        // 暂时获取所有商品数据
        List<SkuInfoEntity> hotSales = baseMapper.getHotSales(pageSize, (page - 1) * pageSize);
        return hotSales;
    }

    /**
     * 获取购物车商品项信息
     *
     * @param skuId
     * @return
     */
    @Override
    public CartSkuItem getCartSkuItem(Long skuId) throws ExecutionException, InterruptedException {
        // 获取sku基本信息
        CartSkuItem item = skuInfoDao.getCartSkuInfoBySkuId(skuId);
        if (item == null) {
            throw new RRException("商品信息不存在,skuId:" + skuId, BizCodeEnum.PRODUCT_NO_EXIST_EXCEPTION.getCode());
        }
        // 会员价
        item.setPrice(item.getOriginalPrice());

        // 获取sku销售属性
        List<SkuSaleAttrValueEntity> saleAttrs = skuSaleAttrValueService.getSaleAttrsBySkuId(skuId);
        if (!CollectionUtils.isEmpty(saleAttrs)) {
            List<AttrBaseVo> attrs = saleAttrs.stream().map(saleAttr -> {
                AttrBaseVo attr = new AttrBaseVo();
                attr.setAttrId(saleAttr.getAttrId());
                attr.setAttrName(saleAttr.getAttrName());
                attr.setAttrValue(saleAttr.getAttrValue());
                return attr;
            }).collect(Collectors.toList());
            item.setAttrs(attrs);
        }
        return item;
    }

    /**
     * 批量获取购物车商品项
     *
     * @param skuIds
     * @return
     */
    @Override
    public List<CartSkuItem> getSkuItems(List<Long> skuIds) {
        if (skuIds.size() == 0) {
            throw new RRException("请选择需要结算的商品", BizCodeEnum.PRODUCT_SERVICE_EXCEPTION.getCode());
        }
        // 获取购物车所需sku基本信息
        List<CartSkuItem> skuItems = skuInfoDao.getCartSkuInfoBatchIds(skuIds);
        if (skuItems == null || skuItems.size() == 0 || skuItems.size() != skuIds.size()) {
            throw new RRException(BizCodeEnum.PRODUCT_NO_EXIST_EXCEPTION.getMsg(), BizCodeEnum.PRODUCT_NO_EXIST_EXCEPTION.getCode());
        }
        for (CartSkuItem skuItem : skuItems) {
            Long skuId = skuItem.getSkuId();
            skuItem.setPrice(skuItem.getOriginalPrice());
            // sku销售属性集合
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
            // 获取优惠信息
            R<SkuPromotionTo> r = couponFeignService.getSkuPromotion(skuId);
            if (!Constant.SUCCESS_CODE.equals(r.getCode())) {
                throw new RRException(r.getMsg(), r.getCode());
            }
            SkuPromotionTo skuPromotionTo = r.getData();
            if (skuPromotionTo != null) {
                skuItem.setFullReductions(skuPromotionTo.getReductions());
                skuItem.setLadders(skuPromotionTo.getLadders());
                if(skuPromotionTo.getBounds() != null){
                    skuItem.setGrowth(skuPromotionTo.getBounds().getGrowBounds());
                    skuItem.setIntegration(skuPromotionTo.getBounds().getBuyBounds());
                }else{
                    skuItem.setGrowth(new BigDecimal(0));
                    skuItem.setIntegration(new BigDecimal(0));
                }
            }
        }
        return skuItems;
    }
}