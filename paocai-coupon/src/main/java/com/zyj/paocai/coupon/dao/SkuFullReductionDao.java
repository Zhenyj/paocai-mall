package com.zyj.paocai.coupon.dao;

import com.zyj.paocai.coupon.entity.SkuFullReductionEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 商品满减信息
 * 
 * @author lulx
 * @email 
 * @date 2022-03-15 21:22:50
 */
@Mapper
public interface SkuFullReductionDao extends BaseMapper<SkuFullReductionEntity> {

    /**
     * 获取sku当前启用的相关满减信息
     * @param skuId
     * @return
     */
    List<SkuFullReductionEntity> getActiveReductionsBySkuId(@Param("skuId") Long skuId);
}
