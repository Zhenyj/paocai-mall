package com.zyj.paocai.product.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zyj.paocai.product.entity.SkuInfoEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * sku信息
 * 
 * @author lulx
 * @email 
 * @date 2022-03-15 21:19:43
 */
@Mapper
public interface SkuInfoDao extends BaseMapper<SkuInfoEntity> {

    List<SkuInfoEntity> getHotSales(@Param("limit") Integer limit, @Param("offset") Integer offset);
}
