package com.zyj.paocai.cart.vo;

import lombok.Data;

/**
 * @author lulx
 * @date 2022-04-24 21:42
 **/
@Data
public class CartItemUpdateVo {
    private Long brandId;
    private Long skuId;
    private Integer count;
}
