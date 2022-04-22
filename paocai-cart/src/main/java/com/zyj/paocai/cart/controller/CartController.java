package com.zyj.paocai.cart.controller;

import com.zyj.paocai.cart.service.CartService;
import com.zyj.paocai.cart.vo.Cart;
import com.zyj.paocai.common.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
     * 获取购物车数据
     * @return
     */
    @GetMapping("/my_cart")
    public R<Cart> getCart(){
        Cart cart = cartService.getCart();
        return R.ok(cart);
    }

    /**
     * 添加商品到购物车（暂用品牌代替店铺）
     * @param brandId 品牌id
     * @param skuId skuId
     * @param num 数量
     * @return
     */
    @GetMapping("/add_to_cart")
    public R addToCart(@RequestParam("brandId")Long brandId,
                       @RequestParam("skuId")Long skuId,
                       @RequestParam("num") Integer num) throws ExecutionException, InterruptedException {
        cartService.addToCart(brandId,skuId,num);
        return R.ok();
    }
}
