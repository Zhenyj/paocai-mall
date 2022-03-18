package com.zyj.paocai.coupon.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zyj.paocai.coupon.entity.HomeAdvEntity;
import com.zyj.paocai.common.utils.PageUtils;

import java.util.Map;

/**
 * 首页轮播广告
 *
 * @author lulx
 * @email 
 * @date 2022-03-15 21:22:50
 */
public interface HomeAdvService extends IService<HomeAdvEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

