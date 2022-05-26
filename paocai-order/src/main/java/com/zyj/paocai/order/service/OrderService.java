package com.zyj.paocai.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zyj.paocai.common.entity.vo.OrderStatusNumsVo;
import com.zyj.paocai.common.utils.PageUtils;
import com.zyj.paocai.order.entity.OrderEntity;
import com.zyj.paocai.order.entity.vo.*;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * 订单
 *
 * @author lulx
 * @email
 * @date 2022-03-15 21:12:53
 */
public interface OrderService extends IService<OrderEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 获取用户订单状态信息
     *
     * @return
     */
    OrderStatusNumsVo getOrderStatusNumsInfo();

    /**
     * 结算并返回订单确认信息
     *
     * @param skuItems
     * @return
     */
    OrderConfirmVo toTrade(List<CartItemBaseVo> skuItems) throws ExecutionException, InterruptedException;

    /**
     * 提交订单、创建订单
     *
     * @param orderSubmitVo
     * @return
     */
    OrderSubmitRespVo submitOrder(OrderSubmitVo orderSubmitVo);

    /**
     * 关闭订单
     *
     * @param order
     */
    void closeOrder(OrderEntity order);

    /**
     * 提交支付订单
     *
     * @param payVo
     * @return
     */
    PayVo getOrderPay(PayVo payVo);

    /**
     * 立即购买单个商品
     *
     * @param vo
     * @return
     */
    OrderConfirmVo toTradeOne(CartItemBaseVo vo) throws ExecutionException, InterruptedException;

    /**
     * 订单列表
     * @param page
     * @param limit
     * @param key
     * @param status
     * @return
     */
    PageUtils getOrderList(Integer page, Integer limit, String key, Integer status);
}

