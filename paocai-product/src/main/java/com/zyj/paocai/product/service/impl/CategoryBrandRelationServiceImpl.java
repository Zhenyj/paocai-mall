package com.zyj.paocai.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zyj.paocai.common.exception.BizCodeEnum;
import com.zyj.paocai.common.utils.PageUtils;
import com.zyj.paocai.common.utils.Query;
import com.zyj.paocai.common.utils.RRException;
import com.zyj.paocai.product.dao.CategoryBrandRelationDao;
import com.zyj.paocai.product.dao.CategoryDao;
import com.zyj.paocai.product.entity.BrandEntity;
import com.zyj.paocai.product.entity.CategoryBrandRelationEntity;
import com.zyj.paocai.product.entity.CategoryEntity;
import com.zyj.paocai.product.entity.vo.BrandVo;
import com.zyj.paocai.product.service.BrandService;
import com.zyj.paocai.product.service.CategoryBrandRelationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service("categoryBrandRelationService")
public class CategoryBrandRelationServiceImpl extends ServiceImpl<CategoryBrandRelationDao, CategoryBrandRelationEntity> implements CategoryBrandRelationService {

    @Autowired
    CategoryDao categoryDao;

    @Autowired
    BrandService brandService;

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

    @Override
    public List<BrandVo> getBrandsByCatId(Long catId) {
        List<CategoryBrandRelationEntity> entities = baseMapper.selectList(
                new QueryWrapper<CategoryBrandRelationEntity>().eq("catelog_id", catId));
        List<BrandVo> brands = entities.stream().map(entity -> {
            BrandVo brandVo = new BrandVo();
            brandVo.setBrandId(entity.getBrandId());
            brandVo.setBrandName(entity.getBrandName());
            return brandVo;
        }).collect(Collectors.toList());

        return brands;
    }

    @Override
    public List<CategoryBrandRelationEntity> getCategoryByBrandId(Long brandId) {
        log.info("获取品牌id:{}的相关分类信息", brandId);
        List<CategoryBrandRelationEntity> entities = this.list(
                new QueryWrapper<CategoryBrandRelationEntity>().eq("brand_id", brandId));
        if (CollectionUtils.isEmpty(entities)) {
            log.info("暂无品牌id:{}的相关分类信息", brandId);
        }
        return entities;
    }

    /**
     * 保存
     *
     * @param categoryBrandRelation
     * @throws RuntimeException
     */
    @Transactional
    @Override
    public void saveDetail(CategoryBrandRelationEntity categoryBrandRelation) throws RuntimeException {
        // 获取分类名称
        Long catelogId = categoryBrandRelation.getCatelogId();
        CategoryEntity category = categoryDao.selectById(catelogId);
        if (category == null) {
            throw new RRException("不存在分类id:" + catelogId + "对应的分类数据", BizCodeEnum.PRODUCT_CATEGORY_NO_EXIST_EXCEPTION.getCode());
        }
        categoryBrandRelation.setCatelogName(category.getName());

        // 获取品牌名称
        Long brandId = categoryBrandRelation.getBrandId();
        BrandEntity brand = brandService.getById(brandId);
        if (brand == null) {
            throw new RRException("不存在品牌id:" + brandId + "对应的分类数据", BizCodeEnum.PRODUCT_CATEGORY_NO_EXIST_EXCEPTION.getCode());
        }
        categoryBrandRelation.setBrandName(brand.getName());
        save(categoryBrandRelation);
    }

    @Override
    public void updateCategory(CategoryEntity category) {
        baseMapper.updateCategory(category.getCatId(), category.getName());
    }

}