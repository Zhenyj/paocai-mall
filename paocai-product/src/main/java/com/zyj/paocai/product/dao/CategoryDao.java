package com.zyj.paocai.product.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zyj.paocai.product.entity.CategoryEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 商品三级分类
 * 
 * @author lulx
 * @email 
 * @date 2022-03-15 21:19:43
 */
@Mapper
public interface CategoryDao extends BaseMapper<CategoryEntity> {

    /**
     * 根据spuId获取分类信息
     * @param spuId
     * @return
     */
    CategoryEntity getCategoryBySpuId(@Param("spuId") Long spuId);
}
