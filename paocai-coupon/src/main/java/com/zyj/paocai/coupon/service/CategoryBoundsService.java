package com.zyj.paocai.coupon.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zyj.paocai.coupon.entity.CategoryBoundsEntity;
import com.zyj.paocai.common.utils.PageUtils;

import java.util.Map;

/**
 * 商品分类积分设置
 *
 * @author lulx
 * @email 
 * @date 2022-03-15 21:22:50
 */
public interface CategoryBoundsService extends IService<CategoryBoundsEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

