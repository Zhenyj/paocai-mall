package com.zyj.paocai.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zyj.paocai.order.entity.RefundInfoEntity;
import com.zyj.paocai.common.utils.PageUtils;

import java.util.Map;

/**
 * 退款信息
 *
 * @author lulx
 * @email 
 * @date 2022-03-15 21:12:53
 */
public interface RefundInfoService extends IService<RefundInfoEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

