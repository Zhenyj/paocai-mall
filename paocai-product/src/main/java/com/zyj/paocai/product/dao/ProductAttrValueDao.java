package com.zyj.paocai.product.dao;

import com.zyj.paocai.common.entity.to.es.SkuEsModel;
import com.zyj.paocai.product.entity.ProductAttrValueEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * spu属性值
 * 
 * @author lulx
 * @email 
 * @date 2022-03-15 21:19:43
 */
@Mapper
public interface ProductAttrValueDao extends BaseMapper<ProductAttrValueEntity> {

    /**
     * 删除spu相关属性
     * @param spuId
     */
    void deleteBySpuId(@Param("spuId") Long spuId);

    List<SkuEsModel.Attrs> getSearchAttrs(@Param("spuId") Long spuId);
}
