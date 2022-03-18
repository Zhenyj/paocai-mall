package com.zyj.paocai.coupon.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zyj.paocai.coupon.entity.CouponHistoryEntity;
import com.zyj.paocai.common.utils.PageUtils;

import java.util.Map;

/**
 * 优惠券领取历史记录
 *
 * @author lulx
 * @email 
 * @date 2022-03-15 21:22:50
 */
public interface CouponHistoryService extends IService<CouponHistoryEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

