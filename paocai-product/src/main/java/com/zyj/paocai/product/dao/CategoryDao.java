package com.zyj.paocai.product.dao;

import com.zyj.paocai.product.entity.CategoryEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 商品三级分类
 * 
 * @author lulx
 * @email 
 * @date 2022-03-15 21:19:43
 */
@Mapper
public interface CategoryDao extends BaseMapper<CategoryEntity> {
	
}
