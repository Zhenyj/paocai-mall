package com.zyj.paocai.common.entity.to;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author lulx
 * @date 2022-04-01 20:37
 **/
@Data
public class SpuBoundsTo {
    private Long spuId;
    private BigDecimal buyBounds;
    private BigDecimal growBounds;
}
