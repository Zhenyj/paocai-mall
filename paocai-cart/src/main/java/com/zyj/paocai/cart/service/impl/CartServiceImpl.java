package com.zyj.paocai.cart.service.impl;

import com.alibaba.fastjson.JSON;
import com.zyj.paocai.cart.constant.CartConstant;
import com.zyj.paocai.cart.feign.ProductFeignService;
import com.zyj.paocai.cart.interceptor.CartInterceptor;
import com.zyj.paocai.cart.service.CartService;
import com.zyj.paocai.cart.vo.Cart;
import com.zyj.paocai.cart.vo.ShopItem;
import com.zyj.paocai.common.constant.Constant;
import com.zyj.paocai.common.entity.vo.BrandVo;
import com.zyj.paocai.common.entity.vo.CartSkuItem;
import com.zyj.paocai.common.entity.vo.MemberRespVo;
import com.zyj.paocai.common.utils.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.LinkedList;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author lulx
 * @date 2022-04-21 22:33
 **/
@Slf4j
@Service
public class CartServiceImpl implements CartService {

    @Autowired
    ProductFeignService productFeignService;

    @Autowired
    ThreadPoolExecutor executor;

    @Autowired
    StringRedisTemplate redisTemplate;

    /**
     * 获取购物车数据
     * @return
     */
    @Override
    public Cart getCart() {
        // 获取当前用户信息
        MemberRespVo member = CartInterceptor.threadLocal.get();
        Long memberId = member.getId();
        if(memberId !=null){
            String cartKey = CartConstant.CART_REDIS_PREFIX+memberId;
        }
        return null;
    }

    /**
     * 添加购物车
     * @param brandId 品牌id
     * @param skuId skuId
     * @param num 数量
     */
    @Override
    public void addToCart(Long brandId, Long skuId, Integer num) throws ExecutionException, InterruptedException {
        // 1、获取用户购物车
        BoundHashOperations<String, String, String> ops = getCartOps();
        // 2、判断是否存在相同brandId的店铺项，存在则添加至已存在的店铺的商品中，否则新增店铺并添加该商品
        String s = ops.get(brandId.toString());
        ShopItem shopItemRedis = JSON.parseObject(s, ShopItem.class);
        if(shopItemRedis == null){
            // 不存在该店铺项，新增店铺并添加该商品
            ShopItem shopItem = new ShopItem();
            // 获取店铺（品牌）信息
            CompletableFuture<Void> brandFuture = CompletableFuture.runAsync(() -> {
                R<BrandVo> r = productFeignService.getBrandInfo(brandId);
                if (!Constant.SUCCESS_CODE.equals(r.getCode())) {
                    throw new RuntimeException("远程获取品牌信息失败");
                }
                BrandVo brandVo = r.getData();
                shopItem.setBrandId(brandId);
                shopItem.setBrandName(brandVo.getName());
            }, executor);

            // 获取商品信息,创建新的商品列表并添加
            CompletableFuture<Void> itemsFuture = CompletableFuture.runAsync(() -> {
                CartSkuItem cartSkuItem = getCartSkuItemBySkuId(skuId);
                cartSkuItem.setCount(num);
                LinkedList<CartSkuItem> items = new LinkedList<>();
                items.add(cartSkuItem);
                shopItem.setItems(items);
            }, executor);

            CompletableFuture.allOf(brandFuture,itemsFuture).get();
            // 3、将新购物车数据替换原有的购物车数据
            ops.put(brandId.toString(), JSON.toJSONString(shopItem));

        }else{
            // 已存在店铺项，判断是否存在skuId相同商品，相同则数量加num，否则新增
            LinkedList<CartSkuItem> items = shopItemRedis.getItems();
            if(CollectionUtils.isEmpty(items)){
                // 如果商品项为空
                CartSkuItem cartSkuItem = getCartSkuItemBySkuId(skuId);
                cartSkuItem.setCount(num);
                items = new LinkedList<>();
                items.add(cartSkuItem);
                shopItemRedis.setItems(items);
                ops.put(brandId.toString(),JSON.toJSONString(shopItemRedis));
                return;
            }
            boolean flag = false;
            for (CartSkuItem item : items) {
                if(skuId.equals(item.getSkuId())){
                    item.setCount(item.getCount()+num);
                    flag = true;
                    break;
                }
            }
            if(!flag){
                CartSkuItem cartSkuItem = getCartSkuItemBySkuId(skuId);
                items.addFirst(cartSkuItem);
            }
            shopItemRedis.setItems(items);
            // 3、将新购物车数据替换原有的购物车数据
            ops.put(brandId.toString(), JSON.toJSONString(shopItemRedis));
        }
    }

    /**
     * 获取购物车商品项信息
     * @param skuId
     * @return
     */
    private CartSkuItem getCartSkuItemBySkuId(Long skuId) {
        R<CartSkuItem> r = productFeignService.getCartSkuItem(skuId);
        if (!Constant.SUCCESS_CODE.equals(r.getCode())) {
            throw new RuntimeException("获取购物车商品信息详情失败");
        }
        return r.getData();
    }

    /**
     *
     * @return
     */
    private BoundHashOperations<String, String, String> getCartOps() {
        MemberRespVo member = CartInterceptor.threadLocal.get();
        if(member.getId() == null){
            throw new RuntimeException("对不起，您还没有登录");
        }
        String cartKey = CartConstant.CART_REDIS_PREFIX + member.getId();
        return redisTemplate.boundHashOps(cartKey);
    }
}
