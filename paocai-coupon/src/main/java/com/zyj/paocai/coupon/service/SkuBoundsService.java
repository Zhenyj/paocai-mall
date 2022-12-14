package com.zyj.paocai.coupon.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zyj.paocai.coupon.entity.SkuBoundsEntity;
import com.zyj.paocai.common.utils.PageUtils;

import java.util.Map;

/**
 * 商品sku积分设置
 *
 * @author lulx
 * @email 
 * @date 2022-03-15 21:22:50
 */
public interface SkuBoundsService extends IService<SkuBoundsEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 根据skuId获取sku积分信息
     * @param skuId
     * @return
     */
    SkuBoundsEntity getBySkuId(Long skuId);

    /**
     * 获取商品sku积分信息
     * @param skuId
     * @return
     */
    SkuBoundsEntity getBoundsBySkuId(Long skuId);
}

