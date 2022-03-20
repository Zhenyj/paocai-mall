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
     * 获取分类规格参数
     *
     * @param catelogId
     * @param key
     * @return
     */
    List<AttrBaseVo> getBaseAttrByCatelogId(@Param("catelogId") Long catelogId, @Param("key") String key);

    /**
     * 查询属性详情
     * @param attrId
     * @return
     */
    AttrInfoVo getAttrInfo(@Param("attrId") Long attrId);
}
