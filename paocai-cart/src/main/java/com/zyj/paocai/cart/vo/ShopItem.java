package com.zyj.paocai.cart.vo;

import com.zyj.paocai.common.entity.vo.CartSkuItem;
import lombok.Data;

import java.util.LinkedList;

/**
 * 购物车店铺项（暂用品牌代替）
 * @author lulx
 * @date 2022-04-21 17:15
 **/
@Data
public class ShopItem {
    private Long brandId;
    private String brandName;

    private LinkedList<CartSkuItem> items;
}
