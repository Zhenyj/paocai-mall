package com.zyj.paocai.common.entity.to;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 商品满减信息
 * @author lulx
 * @date 2022-04-21 17:10
 **/
@Data
public class SkuFullReductionTo {
    private Long id;
    /**
     * spu_id
     */
    private Long skuId;
    /**
     * 满多少
     */
    private BigDecimal fullPrice;
    /**
     * 减多少
     */
    private BigDecimal reducePrice;
    /**
     * 是否参与其他优惠
     */
    private Integer addOther;

    /**
     * 是否启用【0-关闭，1-启用】
     */
    private Integer status;

    /**
     * 开始时间
     */
    private Date startTime;

    /**
     * 结束时间
     */
    private Date expireTime;
}
