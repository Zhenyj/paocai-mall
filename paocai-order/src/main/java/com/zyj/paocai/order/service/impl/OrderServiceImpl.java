package com.zyj.paocai.order.service.impl;

import cn.hutool.core.util.IdUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zyj.paocai.common.constant.Constant;
import com.zyj.paocai.common.entity.to.AddressTo;
import com.zyj.paocai.common.entity.to.CartReleaseOrderItemTo;
import com.zyj.paocai.common.entity.to.OrderTo;
import com.zyj.paocai.common.entity.to.WareLockTo;
import com.zyj.paocai.common.entity.vo.*;
import com.zyj.paocai.common.exception.BizCodeEnum;
import com.zyj.paocai.common.utils.PageUtils;
import com.zyj.paocai.common.utils.Query;
import com.zyj.paocai.common.utils.R;
import com.zyj.paocai.common.utils.RRException;
import com.zyj.paocai.order.config.MQConfig;
import com.zyj.paocai.order.constant.OrderConstant;
import com.zyj.paocai.order.dao.OrderDao;
import com.zyj.paocai.order.entity.OrderEntity;
import com.zyj.paocai.order.entity.OrderItemEntity;
import com.zyj.paocai.order.entity.to.OrderCreateTo;
import com.zyj.paocai.order.entity.vo.CartItemBaseVo;
import com.zyj.paocai.order.entity.vo.OrderConfirmVo;
import com.zyj.paocai.order.entity.vo.OrderInfoVo;
import com.zyj.paocai.order.entity.vo.OrderSubmitVo;
import com.zyj.paocai.order.feign.MemberFeignService;
import com.zyj.paocai.order.feign.ProductFeignService;
import com.zyj.paocai.order.feign.WareFeignService;
import com.zyj.paocai.order.interceptor.LoginInfoInterceptor;
import com.zyj.paocai.order.service.OrderItemService;
import com.zyj.paocai.order.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;


/**
 * 订单业务实现类
 *
 * @author lulx
 * @email
 * @date 2022-03-15 21:12:53
 */
@Slf4j
@Service("orderService")
public class OrderServiceImpl extends ServiceImpl<OrderDao, OrderEntity> implements OrderService {

    private ThreadLocal<OrderSubmitVo> submitVoThreadLocal = new ThreadLocal<>();

    @Autowired
    OrderDao orderDao;

    @Autowired
    OrderItemService orderItemService;

    @Autowired
    ProductFeignService productFeignService;

    @Autowired
    WareFeignService wareFeignService;

    @Autowired
    MemberFeignService memberFeignService;

    @Autowired
    StringRedisTemplate redisTemplate;

    @Autowired
    RabbitTemplate rabbitTemplate;

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
        List<Long> skuIds = new ArrayList<>(skuItems.size());
        //
        List<SkuIdCountVo> skuIdCountVos = new ArrayList<>(skuItems.size());
        // 商品数量
        Map<Long, Integer> skuCountMap = new HashMap<>(skuItems.size());
        for (CartItemBaseVo skuItem : skuItems) {
            skuIds.add(skuItem.getSkuId());
            SkuIdCountVo skuIdCountVo = new SkuIdCountVo();
            skuIdCountVo.setSkuId(skuItem.getSkuId());
            skuIdCountVo.setCount(skuItem.getCount());
            skuIdCountVos.add(skuIdCountVo);
            skuCountMap.put(skuItem.getSkuId(), skuItem.getCount());
        }
        // 获取最新的商品信息
        CompletableFuture<Void> orderInfoFuture = CompletableFuture.runAsync(() -> {
            OrderInfoVo orderInfoVo = getOrderInfoVo(skuIds, skuIdCountVos, skuCountMap);
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
        // 生成防重令牌，使用UUID拼接用户id做到不重复
        String orderToken = UUID.randomUUID().toString().replace("-", "") + member.getId();
        vo.setOrderToken(orderToken);
        redisTemplate.opsForValue().set(OrderConstant.USER_ORDER_TOKEN_PREFIX + orderToken, orderToken,
                60 * 30, TimeUnit.SECONDS);
        return vo;
    }

    /**
     * 获取最新的商品信息
     *
     * @param skuIds      skuId数组
     * @param skuCountMap 商品是
     * @return
     */
    private OrderInfoVo getOrderInfoVo(List<Long> skuIds, List<SkuIdCountVo> skuIdCountVos, Map<Long, Integer> skuCountMap) {
        R<List<CartSkuItem>> r = productFeignService.getSkuItems(skuIds);
        if (!Constant.SUCCESS_CODE.equals(r.getCode())) {
            throw new RRException(r.getMsg(), r.getCode());
        }
        List<CartSkuItem> cartSkuItems = r.getData();
        // 获取库存信息
        R<List<SkuHasStockVo>> skuHasStockBatchResult = wareFeignService.getSkuHasStockBatchWithNums(skuIdCountVos);
        if (!Constant.SUCCESS_CODE.equals(skuHasStockBatchResult.getCode())) {
            throw new RRException(skuHasStockBatchResult.getMsg(), skuHasStockBatchResult.getCode());
        }
        List<SkuHasStockVo> skuHasStockVos = skuHasStockBatchResult.getData();
        if (CollectionUtils.isEmpty(skuHasStockVos) || skuHasStockVos.size() != skuIdCountVos.size()) {
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
            if (!cartSkuItem.getBrandId().equals(tempBrandId)) {
                // 店铺不同
                // 将上一个店铺保存到店铺数组中
                if (shop != null) {
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
        if (shop != null) {
            shop.setItems(items);
            shops.add(shop);
        }

        if (shops == null || shops.size() == 0) {
            throw new RRException(BizCodeEnum.ORDER_NOT_EXIST_SHOP_EXCEPTION.getMsg(), BizCodeEnum.ORDER_NOT_EXIST_SHOP_EXCEPTION.getCode());
        }
        OrderInfoVo orderInfoVo = new OrderInfoVo();
        orderInfoVo.setShops(shops);
        orderInfoVo.calculate();
        return orderInfoVo;
    }

    /**
     * 提交订单、创建订单
     *
     * @param orderSubmitVo
     * @return
     */
    @Transactional
    @Override
    public List<OrderEntity> submitOrder(OrderSubmitVo orderSubmitVo) {
        try {
            MemberRespVo member = LoginInfoInterceptor.loginInfo.get();
            // 1、验证令牌
            log.info("使用lua脚本验证令牌");
            checkOrderToken(orderSubmitVo.getOrderToken(), member.getId());
            // 保存提交的订单信息用于与最新的价格比较
            OrderInfoVo submitVoOrderInfo = orderSubmitVo.getOrderInfo();
            // 2、获取最新的商品信息
            List<Long> skuIds = new ArrayList<>(submitVoOrderInfo.getShops().size() + 1);
            Map<Long, Integer> skuCountMap = new HashMap<>(submitVoOrderInfo.getShops().size() + 1);
            List<SkuIdCountVo> skuIdCountVos = new ArrayList<>(submitVoOrderInfo.getShops().size() + 1);
            for (ShopItem shop : submitVoOrderInfo.getShops()) {
                for (CartSkuItem item : shop.getItems()) {
                    skuIds.add(item.getSkuId());
                    skuCountMap.put(item.getSkuId(), item.getCount());
                    SkuIdCountVo skuIdCountVo = new SkuIdCountVo();
                    skuIdCountVo.setSkuId(item.getSkuId());
                    skuIdCountVo.setSkuName(item.getSkuName());
                    skuIdCountVo.setCount(item.getCount());
                    skuIdCountVos.add(skuIdCountVo);
                }
            }
            OrderInfoVo orderInfoVo = getOrderInfoVo(skuIds, skuIdCountVos, skuCountMap);
            orderSubmitVo.setOrderInfo(orderInfoVo);
            submitVoThreadLocal.set(orderSubmitVo);
            // /3、创建订单，订单项
            List<OrderCreateTo> orders = buildOrder();
            List<String> orderSnList = new ArrayList<>(orders.size());
            List<OrderEntity> result = new ArrayList<>(orders.size());
            for (OrderCreateTo order : orders) {
                result.add(order.getOrder());
                orderSnList.add(order.getOrder().getOrderSn());
            }
            // 4、验证价格,金额对比
            List<ShopItem> submitVoOrderInfoShops = submitVoOrderInfo.getShops();
            for (int i = 0; i < orders.size(); i++) {
                if (orders.get(i).getPayAmount().compareTo(submitVoOrderInfoShops.get(i).getPayAmount()) != 0) {
                    throw new RRException(BizCodeEnum.ORDER_PRICE_ERROR_EXCEPTION.getMsg(),
                            BizCodeEnum.ORDER_PRICE_ERROR_EXCEPTION.getCode());
                }
            }
            // 5、保存订单
            log.info("保存订单:{}", orderSnList);
            doCreateOrder(orders);
            // 6、库存锁定
            log.info("锁定库存:{}", orderSnList);
            orderLockStock(orders);
            // 为了保证高并发 库存服务自己回滚,自动解锁库存（延时队列）
            // 订单创建创建成功发送消息给mq
            for (OrderCreateTo order : orders) {
                log.info("orderSn:{}订单创建成功，发送订单信息到延时队列", order.getOrder().getOrderSn());
                rabbitTemplate.convertAndSend(MQConfig.ORDER_EVENT_EXCHANGE,
                        MQConfig.ORDER_CREATE_ORDER_ROUTING_KEY, order.getOrder());
            }
            log.info("订单创建完成,删除购物车已选中的商品项");
            // TODO 删除购物车已选中的商品项
            CartReleaseOrderItemTo cartReleaseOrderItemTo = new CartReleaseOrderItemTo();
            StringBuilder sb = new StringBuilder();
            for (Long skuId : skuIds) {
                sb.append("," + skuId);
            }
            if (sb.length() > 0) {
                String skuIdStr = sb.substring(1);
                cartReleaseOrderItemTo.setSkuIdStr(skuIdStr);
                cartReleaseOrderItemTo.setMemberId(member.getId());
                try {
                    rabbitTemplate.convertAndSend(MQConfig.ORDER_EVENT_EXCHANGE,
                            MQConfig.CART_RELEASE_ORDER_ITEM_ROUTING_KEY, cartReleaseOrderItemTo);
                } catch (Exception e) {
                    log.error(BizCodeEnum.CART_RELEASE_ORDER_ITEM_EXCEPTION.getMsg() + ":" + e.getMessage());
                }
            }
            return result;
        } finally {
            submitVoThreadLocal.remove();
        }
    }

    /**
     * 关闭订单
     *
     * @param order
     */
    @Transactional
    @Override
    public void closeOrder(OrderEntity order) {
        // 获取订单最新状态
        OrderEntity orderDb = getById(order.getId());
        if (orderDb.getStatus() == OrderConstant.OrderStatusEnum.CREATE_NEW.getStatus()) {
            // 未支付，关闭订单
            OrderEntity orderEntity = new OrderEntity();
            orderEntity.setId(orderDb.getId());
            orderEntity.setOrderSn(orderDb.getOrderSn());
            orderEntity.setStatus(OrderConstant.OrderStatusEnum.CANCEL.getStatus());
            orderEntity.setModifyTime(new Date());
            this.updateById(orderEntity);
            OrderTo orderTo = new OrderTo();
            BeanUtils.copyProperties(orderEntity, orderTo);
            try {
                // TODO 保证消息一定会发送出去，每一个消息都可以做好日志记录（给数据库保存每一个消息的详细信息）
                // TODO 定期扫描数据库将失败的消息再重新发送一遍
                // 发送MQ，通知其他相关功能执行，例如释放库存
                rabbitTemplate.convertAndSend(MQConfig.ORDER_EVENT_EXCHANGE,
                        MQConfig.ORDER_RELEASE_OTHER_ROUTING_KEY, orderTo);
            } catch (Exception e) {
                //TODO 将没发送成功的消息进行重新发送
                log.error(e.getMessage());
            }
        }

    }

    /**
     * 锁定库存
     *
     * @param orderCreateTos
     */
    private void orderLockStock(List<OrderCreateTo> orderCreateTos) {
        ArrayList<String> orderSnList = new ArrayList<>(orderCreateTos.size());
        List<WareLockTo> wareLockTos = orderCreateTos.stream().map((order) -> {
            WareLockTo to = new WareLockTo();
            OrderEntity orderEntity = order.getOrder();
            orderSnList.add(orderEntity.getOrderSn());
            to.setOrderSn(orderEntity.getOrderSn());
            List<SkuIdCountVo> skuIdCountVoList = new ArrayList<>();
            order.getOrderItems().stream().forEach(item -> {
                SkuIdCountVo skuIdCountVo = new SkuIdCountVo();
                skuIdCountVo.setSkuId(item.getSkuId());
                skuIdCountVo.setSkuName(item.getSkuName());
                skuIdCountVo.setCount(item.getSkuQuantity());
                skuIdCountVoList.add(skuIdCountVo);
            });
            to.setSkuIdCountVos(skuIdCountVoList);
            return to;
        }).collect(Collectors.toList());
        R r = wareFeignService.orderLockStock(wareLockTos);
        if (!Constant.SUCCESS_CODE.equals(r.getCode())) {
            throw new RRException(r.getMsg(), r.getCode());
        }
    }

    /**
     * 验证令牌，避免重复提交
     *
     * @param orderToken
     * @param memberId
     * @return
     */
    private void checkOrderToken(String orderToken, Long memberId) {
        // 验证令牌(对比和删除必须保持原子性)，所以使用lua脚本
        String script = "if redis.call(\"get\",KEYS[1]) == ARGV[1]\n" +
                "then\n" +
                "    return redis.call(\"del\",KEYS[1])\n" +
                "else\n" +
                "    return 0\n" +
                "end";
        Long result = redisTemplate.execute(new DefaultRedisScript<>(script, Long.class),
                Arrays.asList(OrderConstant.USER_ORDER_TOKEN_PREFIX + orderToken), orderToken);
        if (result == null || result == 0L) {
            throw new RRException(BizCodeEnum.ORDER_TIME_OUT_EXCEPTION.getMsg(),
                    BizCodeEnum.ORDER_TIME_OUT_EXCEPTION.getCode());
        }
    }

    /**
     * 创建订单
     *
     * @param orderCreateTos
     */
    @Transactional
    public void doCreateOrder(List<OrderCreateTo> orderCreateTos) {
        List<OrderEntity> orders = new ArrayList<>(orderCreateTos.size());
        List<OrderItemEntity> orderItems = new ArrayList<>(orderCreateTos.size() + 1);
        orderCreateTos.stream().forEach(to -> {
            orders.add(to.getOrder());
            orderItems.addAll(to.getOrderItems());
        });
        this.saveBatch(orders);
        orderItemService.saveBatch(orderItems);
    }


    /**
     * 创建订单、订单项
     *
     * @return
     */
    private List<OrderCreateTo> buildOrder() {
        MemberRespVo member = LoginInfoInterceptor.loginInfo.get();
        Long memberId = member.getId();
        OrderSubmitVo orderSubmitVo = submitVoThreadLocal.get();
        OrderInfoVo orderInfo = orderSubmitVo.getOrderInfo();
        List<ShopItem> shops = orderInfo.getShops();
        AddressTo address = orderSubmitVo.getAddress();
        Date date = new Date();
        List<OrderCreateTo> orders = shops.stream().map(shop -> {
            OrderCreateTo orderCreateTo = new OrderCreateTo();
            // 生成订单号，使用雪花算法
            String orderSn = String.valueOf(IdUtil.getSnowflake().nextId());
            log.info("准备构建订单,订单号:{}", orderSn);
            OrderEntity orderEntity = new OrderEntity();
            orderEntity.setMemberId(memberId);
            orderEntity.setOrderSn(orderSn);
            orderEntity.setFreightAmount(shop.getFreightAmount());
            orderCreateTo.setFreightAmount(shop.getFreightAmount());
            orderEntity.setPayAmount(shop.getPayAmount());
            orderCreateTo.setPayAmount(shop.getPayAmount());
            orderEntity.setTotalAmount(shop.getTotalAmount());
            orderEntity.setDeliveryCompany(shop.getDeliveryCompany());
            orderEntity.setPromotionAmount(shop.getPromotionAmount());
            orderEntity.setNote(shop.getNote());
            orderEntity.setGrowth(shop.getGrowth());
            orderEntity.setIntegration(shop.getIntegration());
            orderEntity.setIntegrationAmount(shop.getIntegrationAmount());
            orderEntity.setStatus(OrderConstant.OrderStatusEnum.CREATE_NEW.getStatus());
            orderEntity.setSourceType(OrderConstant.SourceTypeEnum.CREATE_NEW.getStatus());
            orderEntity.setReceiverName(address.getName());
            orderEntity.setReceiverPhone(address.getPhone());
            orderEntity.setReceiverProvince(address.getProvince());
            orderEntity.setReceiverCity(address.getCity());
            orderEntity.setReceiverRegion(address.getRegion());
            orderEntity.setReceiverDetailAddress(address.getDetailAddress());
            orderEntity.setReceiverPostCode(address.getPostCode());
            orderEntity.setCreateTime(date);
            orderEntity.setModifyTime(date);
            orderCreateTo.setOrder(orderEntity);
            // 构建订单项信息
            List<OrderItemEntity> orderItems = buildOrderItems(shop.getItems(), orderSn);
            orderCreateTo.setOrderItems(orderItems);
            return orderCreateTo;
        }).collect(Collectors.toList());

        return orders;
    }

    /**
     * 构建订单项数据
     *
     * @param items
     * @param orderSn
     * @return
     */
    private List<OrderItemEntity> buildOrderItems(List<CartSkuItem> items, String orderSn) {
        List<OrderItemEntity> entities = items.stream().map(item -> {
            OrderItemEntity orderItem = new OrderItemEntity();
            orderItem.setOrderSn(orderSn);
            orderItem.setIntegrationAmount(new BigDecimal(0));
            orderItem.setGiftGrowth(item.getGrowth());
            orderItem.setGiftIntegration(item.getIntegration());
            orderItem.setRealAmount(item.getPayAmount());
            orderItem.setPromotionAmount(item.getPromotionAmount());
            orderItem.setSkuId(item.getSkuId());
            orderItem.setSkuName(item.getSkuName());
            orderItem.setSkuPic(item.getSkuDefaultImg());
            orderItem.setSpuBrand(item.getBrandName());
            orderItem.setSkuQuantity(item.getCount());
            orderItem.setSkuAttrsVals(JSON.toJSONString(item.getAttrs()));
            return orderItem;
        }).collect(Collectors.toList());
        return entities;
    }
}