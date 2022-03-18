package com.zyj.paocai.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zyj.paocai.order.entity.PaymentInfoEntity;
import com.zyj.paocai.common.utils.PageUtils;

import java.util.Map;

/**
 * 支付信息表
 *
 * @author lulx
 * @email 
 * @date 2022-03-15 21:12:53
 */
public interface PaymentInfoService extends IService<PaymentInfoEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

