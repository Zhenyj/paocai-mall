package com.zyj.paocai.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zyj.paocai.order.entity.OrderSettingEntity;
import com.zyj.paocai.common.utils.PageUtils;

import java.util.Map;

/**
 * 订单配置信息
 *
 * @author lulx
 * @email 
 * @date 2022-03-15 21:12:53
 */
public interface OrderSettingService extends IService<OrderSettingEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

