package com.zyj.paocai.order.entity.to;

import com.zyj.paocai.order.entity.OrderEntity;
import com.zyj.paocai.order.entity.OrderItemEntity;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author lulx
 * @date 2022-01-26 15:20
 **/
@Data
public class OrderCreateTo {
    /** 订单信息 */
    private OrderEntity order;
    /** 订单项信息 */
    private List<OrderItemEntity> orderItems;
    /** 应付金额 */
    private BigDecimal payAmount;
    /** 运费 */
    private BigDecimal freightAmount;
}