package com.zyj.paocai.product.dao;

import com.zyj.paocai.product.entity.AttrEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zyj.paocai.product.entity.vo.AttrBaseVo;
import com.zyj.paocai.product.entity.vo.AttrInfoVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 商品属性
 *
 * @author lulx
 * @email
 * @date 2022-03-15 21:19:43
 */
@Mapper
public interface AttrDao extends BaseMapper<AttrEntity> {

    List<AttrEntity> getAttrsByAttrGroupId(@Param("attrGroupId") Long attrGroupId);

    /**
     * 获取分类属性
     *
     * @param catelogId
     * @param key
     * @param attrType
     * @return
     */
    List<AttrBaseVo> getAttrByCatelogId(@Param("catelogId") Long catelogId, @Param("key") String key,@Param("attrType") Integer attrType);

    /**
     * 查询属性详情
     * @param attrId
     * @return
     */
    AttrInfoVo getAttrInfo(@Param("attrId") Long attrId);
}
