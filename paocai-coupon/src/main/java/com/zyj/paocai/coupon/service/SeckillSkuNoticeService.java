package com.zyj.paocai.coupon.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zyj.paocai.coupon.entity.SeckillSkuNoticeEntity;
import com.zyj.paocai.common.utils.PageUtils;

import java.util.Map;

/**
 * 秒杀商品通知订阅
 *
 * @author lulx
 * @email 
 * @date 2022-03-15 21:22:50
 */
public interface SeckillSkuNoticeService extends IService<SeckillSkuNoticeEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

