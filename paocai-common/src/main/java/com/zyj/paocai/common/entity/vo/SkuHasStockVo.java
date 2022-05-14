package com.zyj.paocai.common.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author lulx
 * @date 2022-04-01 22:25
 **/
@AllArgsConstructor
@NoArgsConstructor
@Data
public class SkuHasStockVo {

    private Long skuId;
    private Boolean hasStock;
}
