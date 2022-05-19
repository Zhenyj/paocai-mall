package com.zyj.paocai.order.entity.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 提交订单、创建订单vo
 *
 * @author lulx
 * @date 2022-05-10 9:38
 **/
@Data
public class OrderSubmitRespVo {
    /** 商户订单号 必填 */
    private String out_trade_no;
    /** 订单信息 */
    private OrderInfoVo orderInfo;
    /** 实付金额 */
    private BigDecimal payAmount;
}
