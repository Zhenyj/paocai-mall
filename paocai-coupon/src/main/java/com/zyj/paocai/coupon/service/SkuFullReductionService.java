package com.zyj.paocai.coupon.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zyj.paocai.common.entity.to.SkuReductionTo;
import com.zyj.paocai.coupon.entity.SkuFullReductionEntity;
import com.zyj.paocai.common.utils.PageUtils;

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
     * 保存sku优惠信息
     * @param skuReductionTo
     */
    void saveSkuReduction(SkuReductionTo skuReductionTo);
}

