package com.zyj.paocai.cart.service.impl;

import com.alibaba.fastjson.JSON;
import com.zyj.paocai.cart.constant.CartConstant;
import com.zyj.paocai.cart.feign.CouponFeignService;
import com.zyj.paocai.cart.feign.ProductFeignService;
import com.zyj.paocai.cart.interceptor.CartInterceptor;
import com.zyj.paocai.cart.service.CartService;
import com.zyj.paocai.cart.vo.Cart;
import com.zyj.paocai.cart.vo.ShopItem;
import com.zyj.paocai.common.constant.Constant;
import com.zyj.paocai.common.entity.to.SkuPromotionTo;
import com.zyj.paocai.common.entity.vo.BrandVo;
import com.zyj.paocai.common.entity.vo.CartSkuItem;
import com.zyj.paocai.common.entity.vo.MemberRespVo;
import com.zyj.paocai.common.utils.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.stream.Collectors;

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
    CouponFeignService couponFeignService;

    @Autowired
    ThreadPoolExecutor executor;

    @Autowired
    StringRedisTemplate redisTemplate;

    /**
     * 获取购物车数据
     *
     * @return
     */
    @Override
    public Cart getCart() {
        // 获取当前用户信息
        List<ShopItem> shopItems = getShopItems();
        if (shopItems == null || shopItems.size() == 0) {
            return null;
        }
        Cart cart = new Cart();
        final Integer[] totalCount = {0};
        final Integer[] skuCount = {0};
        BigDecimal totalAmount = new BigDecimal(0);
        for (ShopItem shopItem : shopItems) {
            LinkedList<CartSkuItem> items = shopItem.getItems();
            for (CartSkuItem item : items) {
                Long skuId = item.getSkuId();
                Integer count = item.getCount();
                totalCount[0] += count;
                skuCount[0] += 1;
                // 获取最新的商品信息
                CartSkuItem newItem = getCartSkuItemWithPromotionBySkuId(skuId);
                BeanUtils.copyProperties(newItem,item);
                item.setCount(count);
            }
            // 计算商品的价格、优惠等
            shopItem.calculate();
        }
        cart.setShops(shopItems);
        cart.setSkuCount(skuCount[0]);
        cart.setTotalCount(totalCount[0]);
        cart.setTotalAmount(totalAmount);
        return cart;
    }

    /**
     * 添加购物车
     *
     * @param brandId 品牌id
     * @param skuId   skuId
     * @param num     数量
     */
    @Override
    public void addToCart(Long brandId, Long skuId, Integer num) throws ExecutionException, InterruptedException {
        // 1、获取用户购物车
        BoundHashOperations<String, String, String> ops = getCartOps();
        // 2、判断是否存在相同brandId的店铺项，存在则添加至已存在的店铺的商品中，否则新增店铺并添加该商品
        String s = ops.get(brandId.toString());
        ShopItem shopItemRedis = JSON.parseObject(s, ShopItem.class);
        if (shopItemRedis == null) {
            // 不存在该店铺项，新增店铺并添加该商品
            ShopItem shopItem = new ShopItem();
            // 获取店铺（品牌）信息
            CompletableFuture<Void> brandFuture = CompletableFuture.runAsync(() -> {
                R<BrandVo> r = productFeignService.getBrandInfo(brandId);
                if (!Constant.SUCCESS_CODE.equals(r.getCode())) {
                    throw new RuntimeException("远程获取品牌信息失败");
                }
                BrandVo brandVo = r.getData();
                if (!brandVo.getBrandId().equals(brandId)) {
                    throw new RuntimeException("添加购物车失败，品牌错误");
                }
                shopItem.setBrandId(brandId);
                shopItem.setBrandName(brandVo.getName());
            }, executor);

            // 获取商品信息,创建新的商品列表并添加
            CompletableFuture<Void> itemsFuture = CompletableFuture.runAsync(() -> {
                CartSkuItem cartSkuItem = getCartSkuItemBySkuId(skuId);
                cartSkuItem.setCount(num);
//                cartSkuItem.setDiscount(new BigDecimal(0));
//                cartSkuItem.setOriginalTotalPrice(cartSkuItem.getOriginalPrice().multiply(BigDecimal.valueOf(num)));
//                cartSkuItem.setTotalPrice(cartSkuItem.getOriginalTotalPrice());
                LinkedList<CartSkuItem> items = new LinkedList<>();
                items.add(cartSkuItem);
                shopItem.setItems(items);
            }, executor);

            CompletableFuture.allOf(brandFuture, itemsFuture).get();
            // 3、将新购物车数据替换原有的购物车数据
            ops.put(brandId.toString(), JSON.toJSONString(shopItem));

        } else {
            // 已存在店铺项，判断是否存在skuId相同商品，相同则数量加num，否则新增
            LinkedList<CartSkuItem> items = shopItemRedis.getItems();
            if (CollectionUtils.isEmpty(items)) {
                // 如果商品项为空
                CartSkuItem cartSkuItem = getCartSkuItemBySkuId(skuId);
                cartSkuItem.setCount(num);
//                cartSkuItem.setDiscount(new BigDecimal(0));
//                cartSkuItem.setOriginalTotalPrice(cartSkuItem.getOriginalPrice().multiply(BigDecimal.valueOf(num)));
//                cartSkuItem.setTotalPrice(cartSkuItem.getOriginalTotalPrice());
                items = new LinkedList<>();
                items.add(cartSkuItem);
                shopItemRedis.setItems(items);
                ops.put(brandId.toString(), JSON.toJSONString(shopItemRedis));
                return;
            }
            boolean flag = false;
            for (CartSkuItem item : items) {
                if (skuId.equals(item.getSkuId())) {
                    item.setCount(item.getCount() + num);
                    flag = true;
                    break;
                }
            }
            if (!flag) {
                CartSkuItem cartSkuItem = getCartSkuItemBySkuId(skuId);
                cartSkuItem.setCount(num);
//                cartSkuItem.setDiscount(new BigDecimal(0));
//                cartSkuItem.setOriginalTotalPrice(cartSkuItem.getOriginalPrice().multiply(BigDecimal.valueOf(num)));
//                cartSkuItem.setTotalPrice(cartSkuItem.getOriginalTotalPrice());
                items.addFirst(cartSkuItem);
            }
            shopItemRedis.setItems(items);
            // 3、将新购物车数据替换原有的购物车数据
            ops.put(brandId.toString(), JSON.toJSONString(shopItemRedis));
        }
    }

    /**
     * 获取购物车商品项包含优惠信息
     *
     * @param skuId
     * @return
     */
    private CartSkuItem getCartSkuItemWithPromotionBySkuId(Long skuId) {
        CartSkuItem skuItem = getCartSkuItemBySkuId(skuId);
        if (skuItem == null) {
            throw new RuntimeException("商品不存在或已下架,商品编号:" + skuId);
        }
        R<SkuPromotionTo> r = couponFeignService.getSkuPromotion(skuId);
        if (!Constant.SUCCESS_CODE.equals(r.getCode())) {
            throw new RuntimeException(r.getMsg());
        }
        SkuPromotionTo skuPromotionTo = r.getData();
        if (skuPromotionTo != null) {
            skuItem.setFullReductions(skuPromotionTo.getReductions());
            skuItem.setLadders(skuPromotionTo.getLadders());
        }
        return skuItem;
    }

    /**
     * 获取购物车缓存中的店铺项
     *
     * @return
     */
    private List<ShopItem> getShopItems() {
        BoundHashOperations<String, String, String> ops = getCartOps();
        List<String> values = ops.values();
        if (values != null && values.size() > 0) {
            List<ShopItem> shopItems = values.stream().map(value -> JSON.parseObject(value, ShopItem.class))
                    .collect(Collectors.toList());
            return shopItems;
        } else {
            return null;
        }
    }

    /**
     * 获取购物车商品项信息
     *
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
     * @return
     */
    private BoundHashOperations<String, String, String> getCartOps() {
        MemberRespVo member = CartInterceptor.threadLocal.get();
        if (member.getId() == null) {
            throw new RuntimeException("对不起，您还没有登录");
        }
        String cartKey = CartConstant.CART_REDIS_PREFIX + member.getId();
        return redisTemplate.boundHashOps(cartKey);
    }
}
