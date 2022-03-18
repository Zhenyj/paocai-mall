package com.zyj.paocai.coupon.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zyj.paocai.coupon.entity.CouponEntity;
import com.zyj.paocai.common.utils.PageUtils;

import java.util.Map;

/**
 * 优惠券信息
 *
 * @author lulx
 * @email 
 * @date 2022-03-15 21:22:50
 */
public interface CouponService extends IService<CouponEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

