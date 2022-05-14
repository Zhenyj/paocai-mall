package com.zyj.paocai.ware.entity.vo;

import lombok.Data;

import java.util.List;

/**
 * @author lulx
 * @date 2022-05-11 9:49
 **/
@Data
public class SkuWareHasStock {
    private Long skuId;
    private String skuName;
    private Integer count;
    private String orderSn;
    private List<Long> wareIds;
}
