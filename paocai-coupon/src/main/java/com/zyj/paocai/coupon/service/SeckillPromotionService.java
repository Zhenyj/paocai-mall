package com.zyj.paocai.coupon.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zyj.paocai.coupon.entity.SeckillPromotionEntity;
import com.zyj.paocai.common.utils.PageUtils;

import java.util.Map;

/**
 * 秒杀活动
 *
 * @author lulx
 * @email 
 * @date 2022-03-15 21:22:50
 */
public interface SeckillPromotionService extends IService<SeckillPromotionEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

