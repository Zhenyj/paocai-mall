package com.zyj.paocai.cart.service.impl;

import com.alibaba.fastjson.JSON;
import com.zyj.paocai.cart.constant.CartConstant;
import com.zyj.paocai.cart.feign.CouponFeignService;
import com.zyj.paocai.cart.feign.ProductFeignService;
import com.zyj.paocai.cart.interceptor.CartInterceptor;
import com.zyj.paocai.cart.service.CartService;
import com.zyj.paocai.cart.vo.Cart;
import com.zyj.paocai.cart.vo.CartItemBaseVo;
import com.zyj.paocai.common.constant.Constant;
import com.zyj.paocai.common.entity.to.CartReleaseOrderItemTo;
import com.zyj.paocai.common.entity.to.SkuPromotionTo;
import com.zyj.paocai.common.entity.vo.BrandVo;
import com.zyj.paocai.common.entity.vo.CartSkuItem;
import com.zyj.paocai.common.entity.vo.MemberRespVo;
import com.zyj.paocai.common.entity.vo.ShopItem;
import com.zyj.paocai.common.exception.BizCodeEnum;
import com.zyj.paocai.common.utils.R;
import com.zyj.paocai.common.utils.RRException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
                BeanUtils.copyProperties(newItem, item);
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
     * 删除单个商品
     *
     * @param cartItemBaseVo
     */
    @Override
    public void deleteItem(CartItemBaseVo cartItemBaseVo) {
        BoundHashOperations<String, String, String> ops = getCartOps();
        Long brandId = cartItemBaseVo.getBrandId();
        if (!ops.hasKey(brandId.toString())) {
            throw new RRException("购物车不存在此店铺及商品信息", BizCodeEnum.CART_SERVICE_EXCEPTION.getCode());
        }
        String shopJson = ops.get(brandId.toString());
        ShopItem shopItem = JSON.parseObject(shopJson, ShopItem.class);
        LinkedList<CartSkuItem> items = shopItem.getItems();
        Iterator<CartSkuItem> iterator = items.iterator();
        while (iterator.hasNext()) {
            if (iterator.next().getSkuId().equals(cartItemBaseVo.getSkuId())) {
                iterator.remove();
                break;
            }
        }
        if (CollectionUtils.isEmpty(items)) {
            // 店铺项中没有任何商品，删除此店铺
            ops.delete(brandId.toString());
            return;
        }
        ops.put(brandId.toString(), JSON.toJSONString(shopItem));
    }

    /**
     * 清空购物车
     */
    @Override
    public void clearCart() {
        MemberRespVo member = CartInterceptor.threadLocal.get();
        if (member.getId() == null) {
            throw new RRException(BizCodeEnum.PLEASE_LOGIN.getMsg(), BizCodeEnum.PLEASE_LOGIN.getCode());
        }
        String cartKey = CartConstant.CART_REDIS_PREFIX + member.getId();
        Boolean b = redisTemplate.delete(cartKey);
        if (b) {
            log.info("会员:" + member.getId() + "已清空");
        }
    }

    /**
     * 批量删除
     *
     * @param vos
     */
    @Override
    public void deleteBatch(List<CartItemBaseVo> vos) {
        if (CollectionUtils.isEmpty(vos)) {
            throw new RRException("对不起，您没有选择任何商品，无法删除", BizCodeEnum.CART_SERVICE_EXCEPTION.getCode());
        }
        Map<Long, CartItemBaseVo> skuIdMap = vos.stream().collect(Collectors.toMap(CartItemBaseVo::getSkuId, Function.identity()));
        BoundHashOperations<String, String, String> ops = getCartOps();
        Set<String> keys = ops.keys();
        Iterator<String> keysIterator = keys.iterator();
        while (keysIterator.hasNext()) {
            // 遍历每个店铺项
            String brandId = keysIterator.next();
            String value = ops.get(brandId);
            ShopItem shopItem = JSON.parseObject(value, ShopItem.class);
            LinkedList<CartSkuItem> items = shopItem.getItems();
            Iterator<CartSkuItem> iterator = items.iterator();
            while (iterator.hasNext()) {
                // 判断是否需要删除
                if (skuIdMap.containsKey(iterator.next().getSkuId())) {
                    // 使用迭代器提供的方法删除（安全）
                    iterator.remove();
                }
            }
            if (CollectionUtils.isEmpty(items)) {
                // 店铺没有商品，删除
                ops.delete(brandId);
            } else {
                // 店铺还有商品，覆盖
                ops.put(brandId, JSON.toJSONString(shopItem));
            }
        }
        // TODO 如果删除、覆盖操作出现问题，已执行的redis操作怎么处理
    }

    /**
     * 异步更新购物车（变更商品数量等）
     *
     * @param itemUpdateVo
     * @return
     */
    @Override
    public CartSkuItem asyncUpdateCart(CartItemBaseVo itemUpdateVo) {
        BoundHashOperations<String, String, String> ops = getCartOps();
        String json = ops.get(itemUpdateVo.getBrandId().toString());
        ShopItem shopItem = JSON.parseObject(json, ShopItem.class);
        // 保存更新后的skuItem
        CartSkuItem result = null;
        LinkedList<CartSkuItem> items = shopItem.getItems();
        for (CartSkuItem item : items) {
            if (item.getSkuId().equals(itemUpdateVo.getSkuId())) {
                CartSkuItem cartSkuItemWithPromotion = getCartSkuItemWithPromotionBySkuId(itemUpdateVo.getSkuId());
                BeanUtils.copyProperties(cartSkuItemWithPromotion, item);
                item.setCount(itemUpdateVo.getCount());
                result = item;
                break;
            }
        }
        if (result == null) {
            throw new RRException(BizCodeEnum.CART_PRODUCT_INFO_EXCEPTION.getMsg(),
                    BizCodeEnum.CART_PRODUCT_INFO_EXCEPTION.getCode());
        }
        shopItem.calculate();
        // 更新缓存
        ops.put(itemUpdateVo.getBrandId().toString(), JSON.toJSONString(shopItem));
        // 单独计算后返回
        result.calculate();
        return result;
    }

    /**
     * 删除购物车中已提交订单的商品
     *
     * @param to
     */
    @Override
    public void cartReleaseOrderItem(CartReleaseOrderItemTo to) {
        BoundHashOperations<String, String, String> cartOps = getCartOpsByMemberId(to.getMemberId());
        String[] split = to.getSkuIdStr().split(",");
        List<Long> skuIds = Stream.of(split).map(Long::parseLong).collect(Collectors.toList());
        Set<String> keys = cartOps.keys();
        for (String key : keys) {
            String s = cartOps.get(key);
            ShopItem shopItem = JSON.parseObject(s, ShopItem.class);
            if (shopItem == null) {
                throw new RRException(BizCodeEnum.CART_PRODUCT_INFO_EXCEPTION.getMsg(),
                        BizCodeEnum.CART_PRODUCT_INFO_EXCEPTION.getCode());
            }
            LinkedList<CartSkuItem> items = shopItem.getItems();
            items.removeIf(cartSkuItem -> skuIds.contains(cartSkuItem.getSkuId()));
            if (items.size() == 0) {
                // 删除店铺
                cartOps.delete(key);
            } else {
                // 更新店铺中的商品项
                shopItem.setItems(items);
                cartOps.put(key, JSON.toJSONString(shopItem));
            }
        }
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
                    throw new RRException("远程获取品牌信息失败", BizCodeEnum.CART_SERVICE_EXCEPTION.getCode());
                }
                BrandVo brandVo = r.getData();
                if (!brandVo.getBrandId().equals(brandId)) {
                    throw new RRException("添加购物车失败，品牌错误", BizCodeEnum.CART_SERVICE_EXCEPTION.getCode());
                }
                shopItem.setBrandId(brandId);
                shopItem.setBrandName(brandVo.getName());
            }, executor);

            // 获取商品信息,创建新的商品列表并添加
            CompletableFuture<Void> itemsFuture = CompletableFuture.runAsync(() -> {
                CartSkuItem cartSkuItem = getCartSkuItemBySkuId(skuId);
                cartSkuItem.setCount(num);
                LinkedList<CartSkuItem> items = new LinkedList<>();
                items.addFirst(cartSkuItem);
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
                items = new LinkedList<>();
                items.addFirst(cartSkuItem);
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
                // 添加到第一个
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
            throw new RRException("商品不存在或已下架,商品编号:" + skuId, BizCodeEnum.PRODUCT_NO_EXIST_EXCEPTION.getCode());
        }
        R<SkuPromotionTo> r = couponFeignService.getSkuPromotion(skuId);
        if (!Constant.SUCCESS_CODE.equals(r.getCode())) {
            throw new RRException(r.getMsg(), r.getCode());
        }
        SkuPromotionTo skuPromotionTo = r.getData();
        if (skuPromotionTo != null) {
            skuItem.setFullReductions(skuPromotionTo.getReductions());
            skuItem.setLadders(skuPromotionTo.getLadders());
            if (skuPromotionTo.getBounds() != null) {
                skuItem.setGrowth(skuPromotionTo.getBounds().getGrowBounds());
                skuItem.setIntegration(skuPromotionTo.getBounds().getBuyBounds());
            } else {
                skuItem.setGrowth(new BigDecimal(0));
                skuItem.setIntegration(new BigDecimal(0));
            }
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
            throw new RRException("获取购物车商品信息详情失败", BizCodeEnum.CART_SERVICE_EXCEPTION.getCode());
        }
        return r.getData();
    }

    /**
     * @return
     */
    private BoundHashOperations<String, String, String> getCartOps() {
        MemberRespVo member = CartInterceptor.threadLocal.get();
        if (member.getId() == null) {
            throw new RRException(BizCodeEnum.PLEASE_LOGIN.getMsg(), BizCodeEnum.PLEASE_LOGIN.getCode());
        }
        String cartKey = CartConstant.CART_REDIS_PREFIX + member.getId();
        return redisTemplate.boundHashOps(cartKey);
    }

    /**
     * @param memberId
     * @return
     */
    private BoundHashOperations<String, String, String> getCartOpsByMemberId(Long memberId) {
        String cartKey = CartConstant.CART_REDIS_PREFIX + memberId;
        return redisTemplate.boundHashOps(cartKey);
    }
}
