package com.zyj.paocai.common.entity.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 店铺可选优惠项
 * @author lulx
 * @date 2022-04-29 10:15
 **/
@Data
public class ShopDiscountOptionVo {
    private String discountTitle;
    private BigDecimal discount;
}
