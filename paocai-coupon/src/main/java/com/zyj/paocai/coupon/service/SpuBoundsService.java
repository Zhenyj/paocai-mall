package com.zyj.paocai.coupon.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zyj.paocai.coupon.entity.SpuBoundsEntity;
import com.zyj.paocai.common.utils.PageUtils;

import java.util.Map;

/**
 * 商品spu积分设置
 *
 * @author lulx
 * @email 
 * @date 2022-03-15 21:22:50
 */
public interface SpuBoundsService extends IService<SpuBoundsEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

