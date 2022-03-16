package com.zyj.paocai.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zyj.paocai.utils.PageUtils;
import com.zyj.paocai.entity.CategoryBoundsEntity;

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

