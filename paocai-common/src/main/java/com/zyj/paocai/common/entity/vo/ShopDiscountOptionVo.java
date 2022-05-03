package com.zyj.paocai.common.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 店铺可选优惠项
 *
 * @author lulx
 * @date 2022-04-29 10:15
 **/
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ShopDiscountOptionVo {
    private String discountTitle;
    private BigDecimal discount;

    /**
     * 不使用优惠选项
     *
     * @return
     */
    public static ShopDiscountOptionVo noUseDiscount() {
        return new ShopDiscountOptionVo("不使用优惠", new BigDecimal(0));
    }
}
