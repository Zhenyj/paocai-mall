package com.zyj.paocai.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zyj.paocai.utils.PageUtils;
import com.zyj.paocai.entity.CategoryEntity;

import java.util.Map;

/**
 * 商品三级分类
 *
 * @author lulx
 * @email 
 * @date 2022-03-15 21:19:43
 */
public interface CategoryService extends IService<CategoryEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

