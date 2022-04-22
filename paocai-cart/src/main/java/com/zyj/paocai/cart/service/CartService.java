package com.zyj.paocai.cart.service;

import com.zyj.paocai.cart.vo.Cart;

import java.util.concurrent.ExecutionException;

/**
 * @author lulx
 * @date 2022-04-21 22:32
 **/
public interface CartService {
    /**
     * 添加购物车
     * @param brandId 品牌id
     * @param skuId skuId
     * @param num 数量
     */
    void addToCart(Long brandId, Long skuId, Integer num) throws ExecutionException, InterruptedException;

    /**
     * 获取购物车数据
     * @return
     */
    Cart getCart();

}
