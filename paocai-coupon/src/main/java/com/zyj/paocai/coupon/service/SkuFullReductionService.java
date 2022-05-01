package com.zyj.paocai.coupon.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zyj.paocai.common.entity.to.SkuPromotionTo;
import com.zyj.paocai.common.entity.to.SkuReductionTo;
import com.zyj.paocai.common.utils.PageUtils;
import com.zyj.paocai.coupon.entity.SkuFullReductionEntity;

import java.util.List;
import java.util.Map;

/**
 * 商品满减信息
 *
 * @author lulx
 * @email 
 * @date 2022-03-15 21:22:50
 */
public interface SkuFullReductionService extends IService<SkuFullReductionEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 获取sku当前启用的相关满减信息
     * @param skuId
     * @return
     */
    List<SkuFullReductionEntity> getActiveReductionsBySkuId(Long skuId);

    /**
     * 保存sku优惠信息
     * @param skuReductionTo
     */
    void saveSkuReduction(SkuReductionTo skuReductionTo);

    /**
     * 获取sku优惠信息
     * @param skuId
     * @return
     */
    SkuPromotionTo getSkuPromotion(Long skuId);

    /**
     * 批量获取sku优惠信息
     * @param skuIds
     * @return
     */
    List<SkuPromotionTo> getSkuPromotionBatch(List<Long> skuIds);
}

