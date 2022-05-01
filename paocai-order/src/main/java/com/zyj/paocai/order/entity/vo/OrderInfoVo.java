package com.zyj.paocai.order.entity.vo;

import com.zyj.paocai.common.entity.vo.ShopItem;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author lulx
 * @date 2022-04-29 10:05
 **/
@Data
public class OrderInfoVo {
    private List<ShopItem> shops;
    private BigDecimal totalAmount;
    private BigDecimal freightAmount;
    private BigDecimal integrationAmount;
    private BigDecimal couponAmount;
    private BigDecimal discountAmount;
}
