package com.zyj.paocai.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zyj.paocai.order.entity.OrderReturnReasonEntity;
import com.zyj.paocai.common.utils.PageUtils;

import java.util.Map;

/**
 * 退货原因
 *
 * @author lulx
 * @email 
 * @date 2022-03-15 21:12:53
 */
public interface OrderReturnReasonService extends IService<OrderReturnReasonEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

