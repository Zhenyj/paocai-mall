package com.zyj.paocai.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zyj.paocai.order.entity.OrderEntity;
import com.zyj.paocai.common.utils.PageUtils;

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
}

