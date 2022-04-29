package com.zyj.paocai.order.entity.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author lulx
 * @date 2022-04-29 10:05
 **/
@Data
public class OrderInfoVo {
    private List<OrderShopItem> shops;
    private BigDecimal totalAmount;
    private BigDecimal freightAmount;
    private BigDecimal integrationAmount;
    private BigDecimal couponAmount;
    private BigDecimal discountAmount;
}
