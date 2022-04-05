package com.zyj.paocai.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zyj.paocai.common.constant.Constant;
import com.zyj.paocai.common.entity.to.SkuHasStockVo;
import com.zyj.paocai.common.utils.PageUtils;
import com.zyj.paocai.common.utils.Query;
import com.zyj.paocai.common.utils.R;
import com.zyj.paocai.product.dao.SkuInfoDao;
import com.zyj.paocai.product.entity.SkuImagesEntity;
import com.zyj.paocai.product.entity.SkuInfoEntity;
import com.zyj.paocai.product.entity.SpuInfoDescEntity;
import com.zyj.paocai.product.entity.vo.SkuItemVo;
import com.zyj.paocai.product.feign.WareFeignService;
import com.zyj.paocai.product.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
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

    @Override
    public SkuItemVo getItem(Long skuId) throws ExecutionException, InterruptedException {
        SkuItemVo vo = new SkuItemVo();
        // 获取sku基本信息
        CompletableFuture<SkuInfoEntity> getSkuInfoFuture = CompletableFuture.supplyAsync(() -> {
            SkuInfoEntity skuInfoEntity = getById(skuId);
            if (skuInfoEntity == null) {
                throw new RuntimeException("没有此商品，skuId:" + skuId);
            }
            vo.setInfo(skuInfoEntity);
            return skuInfoEntity;
        }, executor);
        SkuInfoEntity skuInfoEntity = getSkuInfoFuture.get();
        Long spuId = skuInfoEntity.getSpuId();
        Long catalogId = skuInfoEntity.getCatalogId();

        // 获取sku是否有库存
        CompletableFuture<Void> skuStockFuture = CompletableFuture.runAsync(() -> {
            R<SkuHasStockVo> r = wareFeignService.getSkuHasStock(skuId);
            if (!Constant.SUCCESS_CODE.equals(r.getCode())) {
                throw new RuntimeException("远程获取sku库存信息失败," + r.getMsg());
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
            vo.setDesp(desc);
        }, executor);

        // 获取spu规格参数信息
        CompletableFuture<Void> groupAttrsFuture = CompletableFuture.runAsync(() -> {
            List<SkuItemVo.SpuItemAttrGroupVo> groupAttrs = attrGroupService.getAttrGroupWithAttrsBySpuId(spuId, catalogId);
            if (CollectionUtils.isEmpty(groupAttrs)) {
                log.warn("没有相关商品的规格参数信息，spuId:" + spuId);
            }
            vo.setGroupAttrs(groupAttrs);
        }, executor);

        // 获取spu销售属性组合
        CompletableFuture<Void> saleAttrsFuture = CompletableFuture.runAsync(() -> {
            List<SkuItemVo.SkuItemSaleAttrVo> saleAttrs = skuSaleAttrValueService.getSaleAttrsBySpuId(spuId);
            if (CollectionUtils.isEmpty(saleAttrs)) {
                log.info("没有相关商品的销售属性组合，spuId:" + spuId);
            }
            vo.setSaleAttrs(saleAttrs);
        }, executor);

        CompletableFuture.allOf(skuStockFuture, skuImagesFuture, descFuture, groupAttrsFuture, saleAttrsFuture).get();
        return vo;
    }

}