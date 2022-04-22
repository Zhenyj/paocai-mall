package com.zyj.paocai.common.entity.to;

import com.zyj.paocai.common.entity.vo.MemberPrice;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author lulx
 * @date 2022-04-01 21:15
 **/
@Data
public class SkuReductionTo {

    public Long skuId;
    private int fullCount;
    private BigDecimal discount;
    private int countStatus;
    private BigDecimal fullPrice;
    private BigDecimal reducePrice;
    private int priceStatus;
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
    private List<MemberPrice> memberPrice;
}
