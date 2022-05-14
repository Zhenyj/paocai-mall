package com.zyj.paocai.order.entity.vo;

import com.zyj.paocai.order.entity.OrderEntity;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * 提交订单、创建订单vo
 *
 * @author lulx
 * @date 2022-05-10 9:38
 **/
@Data
public class OrderSubmitRespVo {
    /** 订单信息 */
    private List<OrderEntity> orders;
    /** 实付金额 */
    private BigDecimal payAmount;
}
