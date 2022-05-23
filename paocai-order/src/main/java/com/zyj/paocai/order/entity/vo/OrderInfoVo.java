package com.zyj.paocai.order.entity.vo;

import com.zyj.paocai.common.entity.vo.ShopItem;
import com.zyj.paocai.common.utils.RRException;
import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @author lulx
 * @date 2022-04-29 10:05
 **/
@Data
public class OrderInfoVo {
    private List<ShopItem> shops = new ArrayList<>(1);
    /** 订单原价 */
    private BigDecimal totalAmount;
    /** 实付金额 */
    private BigDecimal payAmount;
    /** 总运费 */
    private BigDecimal freightAmount;
    /** 积分抵扣金额（总） */
    private BigDecimal integrationAmount;
    /** 下单可得积分（总） */
    private BigDecimal integration;
    /** 下单可得会员成长值（总） */
    private BigDecimal growth;
    /** 优惠券金额 */
    private BigDecimal couponAmount;
    /** 后台调整订单使用的折扣金额 */
    private BigDecimal discountAmount;

    /**
     * 计算价格
     */
    public void calculate() {
        if (shops == null || shops.size() == 0) {
            throw new RRException("订单信息中，不存在店铺或商品");
        }
        BigDecimal totalAmount = new BigDecimal(0);
        BigDecimal payAmount = new BigDecimal(0);
        BigDecimal freightAmount = new BigDecimal(0);
        BigDecimal integrationAmount = new BigDecimal(0);
        BigDecimal couponAmount = new BigDecimal(0);
        BigDecimal discountAmount = new BigDecimal(0);
        BigDecimal integration = new BigDecimal(0);
        BigDecimal growth = new BigDecimal(0);
        for (ShopItem shop : shops) {
            shop.calculate();
            totalAmount = totalAmount.add(shop.getTotalAmount());
            payAmount = payAmount.add(shop.getPayAmount());
            freightAmount = freightAmount.add(shop.getFreightAmount());
            integrationAmount = integrationAmount.add(shop.getIntegrationAmount());
            integration = integration.add(shop.getIntegration());
            couponAmount = couponAmount.add(shop.getCouponAmount());
            discountAmount = discountAmount.add(shop.getDiscountAmount());
            growth = growth.add(shop.getGrowth());
        }
        this.totalAmount = totalAmount;
        this.payAmount = payAmount;
        this.freightAmount = freightAmount;
        this.integrationAmount = integrationAmount;
        this.couponAmount = couponAmount;
        this.discountAmount = discountAmount;
        this.integration = integration;
        this.growth = growth;
    }

    public void addShop(ShopItem shopItem){
        shops.add(shopItem);
    }
}
