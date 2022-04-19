package com.zyj.paocai.product.dao;

import com.zyj.paocai.product.entity.BrandEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 品牌
 *
 * @author lulx
 * @email
 * @date 2022-03-15 21:19:43
 */
@Mapper
public interface BrandDao extends BaseMapper<BrandEntity> {

    /**
     * 通过spuId获取品牌信息
     *
     * @param spuId
     * @return
     */
    BrandEntity getBrandBySpuId(@Param("spuId") Long spuId);
}
