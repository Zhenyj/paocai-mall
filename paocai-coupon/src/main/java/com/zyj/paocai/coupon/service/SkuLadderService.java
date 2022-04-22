package com.zyj.paocai.coupon.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zyj.paocai.coupon.entity.SkuLadderEntity;
import com.zyj.paocai.common.utils.PageUtils;

import java.util.List;
import java.util.Map;

/**
 * 商品阶梯价格
 *
 * @author lulx
 * @email 
 * @date 2022-03-15 21:22:50
 */
public interface SkuLadderService extends IService<SkuLadderEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 获取sku当前启用的相关阶梯价格信息
     * @param skuId
     * @return
     */
    List<SkuLadderEntity> getActiveLaddersBySkuId(Long skuId);
}

