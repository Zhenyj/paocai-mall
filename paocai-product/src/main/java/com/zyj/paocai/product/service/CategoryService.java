package com.zyj.paocai.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zyj.paocai.product.entity.CategoryEntity;
import com.zyj.paocai.common.utils.PageUtils;

import java.util.List;
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

    /**
     * 获取三级分类
     * @return
     */
    List<CategoryEntity> listTree();

}

