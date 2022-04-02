package com.zyj.paocai.product.dao;

import com.zyj.paocai.product.entity.AttrGroupEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zyj.paocai.product.entity.vo.AttrGroupWithAttrsVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 属性分组
 * 
 * @author lulx
 * @email 
 * @date 2022-03-15 21:19:43
 */
@Mapper
public interface AttrGroupDao extends BaseMapper<AttrGroupEntity> {

    /**
     * 获取分类下所有分组&关联属性
     * @param catelogId
     * @return
     */
    List<AttrGroupWithAttrsVo> getAttrGroupWithAttrs(@Param("catelogId") Long catelogId);
}
