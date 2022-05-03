package com.zyj.paocai.cart.controller;

import com.zyj.paocai.cart.service.CartService;
import com.zyj.paocai.cart.vo.Cart;
import com.zyj.paocai.cart.vo.CartItemBaseVo;
import com.zyj.paocai.common.entity.vo.CartSkuItem;
import com.zyj.paocai.common.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * @author lulx
 * @date 2022-04-21 22:32
 **/
@RestController
public class CartController {

    @Autowired
    CartService cartService;

    /**
     * 异步更新购物车（变更商品数量等）
     *
     * @param itemUpdateVo
     * @return
     */
    @PostMapping("/async_update_cart")
    public R<CartSkuItem> asyncUpdateCart(@RequestBody CartItemBaseVo itemUpdateVo) {
        CartSkuItem item = cartService.asyncUpdateCart(itemUpdateVo);
        return R.ok(item);
    }

    /**
     * 清空购物车
     *
     * @return
     */
    @GetMapping("/clear_cart")
    public R clearCart() {
        cartService.clearCart();
        return R.ok();
    }

    /**
     * 批量删除
     *
     * @return
     */
    @PostMapping("/delete_batch")
    public R deleteBatch(@RequestBody List<CartItemBaseVo> vos) {
        cartService.deleteBatch(vos);
        return R.ok();
    }

    /**
     * 删除单个商品
     *
     * @param
     * @return
     */
    @PostMapping("/delete_item")
    public R deleteItem(@RequestBody CartItemBaseVo cartItemBaseVo) {
        cartService.deleteItem(cartItemBaseVo);
        return R.ok();
    }

    /**
     * 获取购物车数据
     *
     * @return
     */
    @GetMapping("/my_cart")
    public R<Cart> getCart() {
        Cart cart = cartService.getCart();
        return R.ok(cart);
    }

    /**
     * 添加商品到购物车（暂用品牌代替店铺）
     *
     * @param brandId 品牌id
     * @param skuId   skuId
     * @param num     数量
     * @return
     */
    @GetMapping("/add_to_cart")
    public R addToCart(@RequestParam("brandId") Long brandId,
                       @RequestParam("skuId") Long skuId,
                       @RequestParam("num") Integer num) throws ExecutionException, InterruptedException {
        cartService.addToCart(brandId, skuId, num);
        return R.ok();
    }
}
