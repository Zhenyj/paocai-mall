package com.zyj.paocai.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zyj.paocai.common.utils.PageUtils;
import com.zyj.paocai.product.entity.CategoryBrandRelationEntity;
import com.zyj.paocai.product.entity.vo.BrandVo;

import java.util.List;
import java.util.Map;

/**
 * 品牌分类关联
 *
 * @author lulx
 * @email
 * @date 2022-03-15 21:19:43
 */
public interface CategoryBrandRelationService extends IService<CategoryBrandRelationEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 根据分类id获取相关品牌
     *
     * @param catId
     * @return
     */
    List<BrandVo> getBrandsByCatId(Long catId);
}

