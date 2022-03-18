package com.zyj.paocai.coupon.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zyj.paocai.coupon.entity.SeckillSkuRelationEntity;
import com.zyj.paocai.common.utils.PageUtils;

import java.util.Map;

/**
 * 秒杀活动商品关联
 *
 * @author lulx
 * @email 
 * @date 2022-03-15 21:22:50
 */
public interface SeckillSkuRelationService extends IService<SeckillSkuRelationEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

