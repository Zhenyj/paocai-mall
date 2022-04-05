package com.zyj.paocai.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zyj.paocai.common.utils.PageUtils;
import com.zyj.paocai.product.entity.CategoryEntity;
import com.zyj.paocai.product.entity.vo.Catalog2Vo;

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

    /**
     * 获取分类完整路径[父，子，孙]
     * @param catelogId
     * @return
     */
    Long[] getCatelogPath(Long catelogId);

    /**
     * 查询所有一级分类
     * @return
     */
    List<CategoryEntity> getLevel1Categorys();

    Map<String, List<Catalog2Vo>> getCatalogJson();

    /**
     * 级联更新所有关联的数据
     * @param category
     */
    void updateCascade(CategoryEntity category);
}

