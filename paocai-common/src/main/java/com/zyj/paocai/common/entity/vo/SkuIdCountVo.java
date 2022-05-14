package com.zyj.paocai.common.entity.vo;

import lombok.Data;

/**
 * 商品id、数量vo
 * @author lulx
 * @date 2022-05-10 14:37
 **/
@Data
public class SkuIdCountVo {
    private Long skuId;
    private String skuName;
    private Integer count;

    public SkuIdCountVo() {
    }

    public SkuIdCountVo(Long skuId, String skuName, Integer count) {
        this.skuId = skuId;
        this.skuName = skuName;
        this.count = count;
    }
}
