package com.zyj.paocai.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zyj.paocai.common.entity.vo.CartSkuItem;
import com.zyj.paocai.common.entity.vo.OrderStatusNumsVo;
import com.zyj.paocai.order.entity.OrderEntity;
import com.zyj.paocai.common.utils.PageUtils;
import com.zyj.paocai.order.entity.vo.OrderConfirmVo;

import java.util.List;
import java.util.Map;

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
     * @param memberId
     * @return
     */
    OrderStatusNumsVo getOrderStatusNumsInfo(Long memberId);

    /**
     * 结算并返回订单确认信息
     * @param skuItems
     * @return
     */
    OrderConfirmVo toTrade(List<CartSkuItem> skuItems);
}

