package com.zyj.paocai.cart.vo;

import com.zyj.paocai.common.entity.vo.CartSkuItem;
import lombok.Data;

import java.math.BigDecimal;
import java.util.LinkedList;

/**
 * 购物车店铺项（暂用品牌代替）
 *
 * @author lulx
 * @date 2022-04-21 17:15
 **/
@Data
public class ShopItem {
    /** 品牌id */
    private Long brandId;
    /** 品牌名 */
    private String brandName;
    /** 该品牌下的总优惠 */
    private BigDecimal discount;
    /**原总价*/
    private BigDecimal originalTotalPrice;
    /** 该品牌下的总价 */
    private BigDecimal totalPrice;
    /** 商品列表 */
    private LinkedList<CartSkuItem> items;

    /**
     * 计算店铺价格及优惠
     */
    public void calculate() {
        BigDecimal originalTotalPrice = new BigDecimal(0);
        BigDecimal totalPrice = new BigDecimal(0);
        BigDecimal discount = new BigDecimal(0);
        for (CartSkuItem item : items) {
            item.calculate();
            originalTotalPrice = originalTotalPrice.add(item.getOriginalTotalPrice());
            totalPrice = totalPrice.add(item.getTotalPrice());
            discount = discount.add(item.getDiscount());
        }
        this.discount = discount;
        this.originalTotalPrice = originalTotalPrice;
        this.totalPrice = totalPrice;
    }
}
