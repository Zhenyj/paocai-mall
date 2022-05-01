package com.zyj.paocai.order.entity.vo;

import lombok.Data;

/**
 * 购物车结算vo
 * @author lulx
 * @date 2022-04-29 17:03
 **/
@Data
public class SkuIdCountVo {
    private Long skuId;
    private Integer count;
}
