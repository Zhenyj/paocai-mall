package com.zyj.paocai.cart.service;

import com.zyj.paocai.cart.vo.Cart;
import com.zyj.paocai.cart.vo.CartItemIdVo;

import java.util.List;
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

    /**
     * 删除单个商品
     * @param cartItemIdVo
     */
    void deleteItem(CartItemIdVo cartItemIdVo);

    /**
     * 清空购物车
     */
    void clearCart();

    /**
     * 批量删除
     * @param vos
     */
    void deleteBatch(List<CartItemIdVo> vos);
}
