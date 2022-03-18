package com.zyj.paocai.coupon.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zyj.paocai.coupon.entity.SeckillSessionEntity;
import com.zyj.paocai.common.utils.PageUtils;

import java.util.Map;

/**
 * 秒杀活动场次
 *
 * @author lulx
 * @email 
 * @date 2022-03-15 21:22:50
 */
public interface SeckillSessionService extends IService<SeckillSessionEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

