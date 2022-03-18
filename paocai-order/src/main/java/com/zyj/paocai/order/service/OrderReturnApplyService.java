package com.zyj.paocai.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zyj.paocai.order.entity.OrderReturnApplyEntity;
import com.zyj.paocai.common.utils.PageUtils;

import java.util.Map;

/**
 * 订单退货申请
 *
 * @author lulx
 * @email 
 * @date 2022-03-15 21:12:53
 */
public interface OrderReturnApplyService extends IService<OrderReturnApplyEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

