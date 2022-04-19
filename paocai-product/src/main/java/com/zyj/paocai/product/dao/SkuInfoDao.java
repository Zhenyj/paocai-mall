package com.zyj.paocai.product.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zyj.paocai.common.entity.vo.SkuDetailVo;
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

    /**
     * 获取sku详细信息
     * @param skuId
     * @return
     */
    SkuDetailVo getSkuDetail(@Param("skuId") Long skuId);

    /**
     * 通过搜索关键字获取相关sku
     * @param keyword
     * @return
     */
    List<SkuInfoEntity> searchSkuByKeyword(@Param("keyword") String keyword);

    /**
     * 通过搜索关键字和三级分类获取相关sku
     * @param catalogId
     * @return
     */
    List<SkuInfoEntity> searchSkuByCatalogId(@Param("catalogId") Long catalogId);
}
