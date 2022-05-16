package com.zyj.paocai.cart.service;

import com.zyj.paocai.cart.vo.Cart;
import com.zyj.paocai.cart.vo.CartItemBaseVo;
import com.zyj.paocai.common.entity.to.CartReleaseOrderItemTo;
import com.zyj.paocai.common.entity.vo.CartSkuItem;

import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * @author lulx
 * @date 2022-04-21 22:32
 **/
public interface CartService {
    /**
     * 添加购物车
     *
     * @param brandId 品牌id
     * @param skuId   skuId
     * @param num     数量
     */
    void addToCart(Long brandId, Long skuId, Integer num) throws ExecutionException, InterruptedException;

    /**
     * 获取购物车数据
     *
     * @return
     */
    Cart getCart();

    /**
     * 删除单个商品
     *
     * @param cartItemBaseVo
     */
    void deleteItem(CartItemBaseVo cartItemBaseVo);

    /**
     * 清空购物车
     */
    void clearCart();

    /**
     * 批量删除
     *
     * @param vos
     */
    void deleteBatch(List<CartItemBaseVo> vos);

    /**
     * 异步更新购物车（变更商品数量等）
     *
     * @param itemUpdateVo
     * @return
     */
    CartSkuItem asyncUpdateCart(CartItemBaseVo itemUpdateVo);

    /**
     * 删除购物车中已提交订单的商品
     *
     * @param skuIds
     */
    void cartReleaseOrderItem(CartReleaseOrderItemTo skuIds);
}
