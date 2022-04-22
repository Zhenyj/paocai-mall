package com.zyj.paocai.common.entity.to;

import lombok.Data;

import java.util.List;

/**
 * sku优惠信息封装
 * @author lulx
 * @date 2022-04-22 9:38
 **/
@Data
public class SkuPromotionTo {

    /**sku满减信息*/
    private List<SkuFullReductionTo> reductions;
    /**sku阶梯价格(满几件打几折)*/
    private List<SkuLadderTo> ladders;
}
