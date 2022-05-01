package com.zyj.paocai.cart.vo;

import com.zyj.paocai.common.entity.vo.ShopItem;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * 购物车实体类
 * @author lulx
 * @date 2022-04-21 17:14
 **/
@Data
public class Cart {
    /**店铺*/
    private List<ShopItem> shops;

    /**商品数量*/
    private Integer totalCount;

    /**sku总数*/
    private Integer skuCount;

    /**原商品总价*/
    private BigDecimal totalAmount;

    /** 商品总价 */
    private BigDecimal payAmount;

    /**已优惠金额*/
    private BigDecimal discount;
}
