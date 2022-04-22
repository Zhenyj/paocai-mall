package com.zyj.paocai.coupon.dao;

import com.zyj.paocai.coupon.entity.SkuLadderEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 商品阶梯价格
 * 
 * @author lulx
 * @email 
 * @date 2022-03-15 21:22:50
 */
@Mapper
public interface SkuLadderDao extends BaseMapper<SkuLadderEntity> {

    /**
     * 获取sku当前启用的相关阶梯价格信息
     * @param skuId
     * @return
     */
    List<SkuLadderEntity> getActiveLaddersBySkuId(@Param("skuId") Long skuId);
}
