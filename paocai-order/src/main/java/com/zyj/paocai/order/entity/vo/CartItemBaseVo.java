package com.zyj.paocai.order.entity.vo;

import lombok.Data;

/**
 * @author lulx
 * @date 2022-04-24 21:42
 **/
@Data
public class CartItemBaseVo {
    private Long brandId;
    private Long skuId;
    private Integer count;
}
