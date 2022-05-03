package com.zyj.paocai.order.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zyj.paocai.common.constant.Constant;
import com.zyj.paocai.common.entity.to.AddressTo;
import com.zyj.paocai.common.entity.vo.*;
import com.zyj.paocai.common.exception.BizCodeEnum;
import com.zyj.paocai.common.utils.PageUtils;
import com.zyj.paocai.common.utils.Query;
import com.zyj.paocai.common.utils.R;
import com.zyj.paocai.common.utils.RRException;
import com.zyj.paocai.order.dao.OrderDao;
import com.zyj.paocai.order.entity.OrderEntity;
import com.zyj.paocai.order.entity.vo.CartItemBaseVo;
import com.zyj.paocai.order.entity.vo.OrderConfirmVo;
import com.zyj.paocai.order.entity.vo.OrderInfoVo;
import com.zyj.paocai.order.feign.MemberFeignService;
import com.zyj.paocai.order.feign.ProductFeignService;
import com.zyj.paocai.order.feign.WareFeignService;
import com.zyj.paocai.order.interceptor.LoginInfoInterceptor;
import com.zyj.paocai.order.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.function.Function;
import java.util.stream.Collectors;


@Slf4j
@Service("orderService")
public class OrderServiceImpl extends ServiceImpl<OrderDao, OrderEntity> implements OrderService {

    @Autowired
    OrderDao orderDao;

    @Autowired
    ProductFeignService productFeignService;

    @Autowired
    WareFeignService wareFeignService;

    @Autowired
    MemberFeignService memberFeignService;

    @Autowired
    ThreadPoolExecutor executor;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<OrderEntity> page = this.page(
                new Query<OrderEntity>().getPage(params),
                new QueryWrapper<OrderEntity>()
        );

        return new PageUtils(page);
    }

    /**
     * 获取用户订单状态信息
     *
     * @return
     */
    @Override
    public OrderStatusNumsVo getOrderStatusNumsInfo() {
        MemberRespVo member = LoginInfoInterceptor.loginInfo.get();
        OrderStatusNumsVo vo = orderDao.getOrderStatusNumsInfo(member.getId());
        // TODO 获取用户购物车商品数量(总数)
        return vo;
    }

    /**
     * 结算并返回订单确认信息
     *
     * @param skuItems
     * @return
     */
    @Override
    public OrderConfirmVo toTrade(List<CartItemBaseVo> skuItems) throws ExecutionException, InterruptedException {
        // TODO 大多数都是购买单件商品，可以单独实现，避免复杂的判断逻辑等
        OrderConfirmVo vo = new OrderConfirmVo();
        MemberRespVo member = LoginInfoInterceptor.loginInfo.get();
        // 商品skuId数组
        List<Long> skuIds = skuItems.stream().map(CartItemBaseVo::getSkuId).collect(Collectors.toList());
        // 商品数量
        Map<Long, Integer> skuCountMap = skuItems.stream().collect(Collectors.toMap(CartItemBaseVo::getSkuId, CartItemBaseVo::getCount));
        // 获取最新的商品信息
        CompletableFuture<Void> orderInfoFuture = CompletableFuture.runAsync(() -> {
            R<List<CartSkuItem>> r = productFeignService.getSkuItems(skuIds);
            if (!Constant.SUCCESS_CODE.equals(r.getCode())) {
                throw new RRException(r.getMsg(), r.getCode());
            }
            List<CartSkuItem> cartSkuItems = r.getData();
            // 获取库存信息
            R<List<SkuHasStockVo>> skuHasStockBatchResult = wareFeignService.getSkuHasStockBatch(skuIds);
            if (!Constant.SUCCESS_CODE.equals(skuHasStockBatchResult.getCode())) {
                throw new RRException(skuHasStockBatchResult.getMsg(), skuHasStockBatchResult.getCode());
            }
            List<SkuHasStockVo> skuHasStockVos = skuHasStockBatchResult.getData();
            if (CollectionUtils.isEmpty(skuHasStockVos) || skuHasStockVos.size() != skuIds.size()) {
                throw new RRException(BizCodeEnum.PRODUCT_WARE_EXCEPTION.getMsg(), BizCodeEnum.PRODUCT_WARE_EXCEPTION.getCode());
            }
            // 转换为skuId为键的map集合
            Map<Long, SkuHasStockVo> hasStockVoMap = skuHasStockVos.stream()
                    .collect(Collectors.toMap(SkuHasStockVo::getSkuId, Function.identity()));
            // 封装商品信息
            cartSkuItems = cartSkuItems.stream()
                    .sorted((o1, o2) -> (int) (o1.getBrandId() - o2.getBrandId())).collect(Collectors.toList());
            // 存放上一次遍历的brandId
            Long tempBrandId = -1L;
            // 临时店铺
            ShopItem shop = null;
            // 店铺数组
            List<ShopItem> shops = new ArrayList<>();
            // 临时商品数组
            LinkedList<CartSkuItem> items = new LinkedList<>();
            for (CartSkuItem cartSkuItem : cartSkuItems) {
                if(!cartSkuItem.getBrandId().equals(tempBrandId)){
                    // 店铺不同
                    // 将上一个店铺保存到店铺数组中
                    if(shop != null){
                        shop.setItems(items);
                        shops.add(shop);
                    }
                    tempBrandId = cartSkuItem.getBrandId();
                    // 新的店铺
                    shop = new ShopItem();
                    shop.setBrandId(cartSkuItem.getBrandId());
                    shop.setBrandName(cartSkuItem.getBrandName());
                    items = new LinkedList<>();
                }
                // 是否有库存
                cartSkuItem.setHasStock(hasStockVoMap.get(cartSkuItem.getSkuId()).getHasStock());
                cartSkuItem.setCount(skuCountMap.get(cartSkuItem.getSkuId()));
                items.add(cartSkuItem);
            }
            if(shop != null){
                shop.setItems(items);
                shops.add(shop);
            }

            if(shops==null || shops.size() == 0){
                throw new RRException(BizCodeEnum.ORDER_NOT_EXIST_SHOP_EXCEPTION.getMsg(),BizCodeEnum.ORDER_NOT_EXIST_SHOP_EXCEPTION.getCode());
            }
            OrderInfoVo orderInfoVo = new OrderInfoVo();
            orderInfoVo.setShops(shops);
            orderInfoVo.calculate();
            vo.setOrderInfo(orderInfoVo);
        }, executor);


        // 获取收获地址,远程调用会丢失session造成缺失用户登录信息
        // 执行异步调用时，会丢失请求数据，提前获取原始请求数据
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        CompletableFuture<Void> addressFuture = CompletableFuture.runAsync(() -> {
            // 在异步线程中，设置之前保存的请求数据，避免丢失请求头等问题
            RequestContextHolder.setRequestAttributes(requestAttributes);
            R<List<AddressTo>> r = memberFeignService.getUserReceiveAddress();
            if (!Constant.SUCCESS_CODE.equals(r.getCode())) {
                throw new RRException(r.getMsg(), r.getCode());
            }
            List<AddressTo> address = r.getData();
            vo.setAddressList(address);
        }, executor);

        CompletableFuture.allOf(addressFuture, orderInfoFuture).get();

        return vo;
    }

}