package com.zyj.paocai.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zyj.paocai.product.dao.CategoryBrandRelationDao;
import com.zyj.paocai.product.entity.CategoryBrandRelationEntity;
import com.zyj.paocai.product.service.CategoryBrandRelationService;
import com.zyj.paocai.common.utils.PageUtils;
import com.zyj.paocai.common.utils.Query;
import org.springframework.stereotype.Service;

import java.util.Map;


@Service("categoryBrandRelationService")
public class CategoryBrandRelationServiceImpl extends ServiceImpl<CategoryBrandRelationDao, CategoryBrandRelationEntity> implements CategoryBrandRelationService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {

        QueryWrapper<CategoryBrandRelationEntity> wrapper = new QueryWrapper<>();
        if (params.get("catId") != null) {
            wrapper.eq("catelogId", params.get("catId"));
        }

        IPage<CategoryBrandRelationEntity> page = this.page(
                new Query<CategoryBrandRelationEntity>().getPage(params),
                wrapper
        );

        return new PageUtils(page);
    }

}