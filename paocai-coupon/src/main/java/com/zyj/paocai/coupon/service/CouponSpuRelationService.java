package com.zyj.paocai.coupon.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zyj.paocai.coupon.entity.CouponSpuRelationEntity;
import com.zyj.paocai.common.utils.PageUtils;

import java.util.Map;

/**
 * 优惠券与产品关联
 *
 * @author lulx
 * @email 
 * @date 2022-03-15 21:22:50
 */
public interface CouponSpuRelationService extends IService<CouponSpuRelationEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

