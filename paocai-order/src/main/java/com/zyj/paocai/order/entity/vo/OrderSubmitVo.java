package com.zyj.paocai.order.entity.vo;

import com.zyj.paocai.common.entity.to.AddressTo;
import com.zyj.paocai.order.constant.OrderConstant;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 提交订单、创建订单vo
 *
 * @author lulx
 * @date 2022-05-10 9:38
 **/
@Data
public class OrderSubmitVo {
    /** 订单信息 */
    private OrderInfoVo orderInfo;
    /** 收货地址 */
    private AddressTo address;
    /** 实付金额 */
    private BigDecimal payAmount;
    /** 令牌 */
    private String orderToken;
    /** 提交方式，【0:购物车方式，1:立即购买方式】 */
    private int submitType = OrderConstant.SubmitTypeEnum.CART.getCode();
}
