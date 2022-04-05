package com.zyj.paocai.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zyj.paocai.common.utils.PageUtils;
import com.zyj.paocai.product.entity.AttrEntity;
import com.zyj.paocai.product.entity.vo.AttrInfoVo;

import java.util.List;
import java.util.Map;

/**
 * 商品属性
 *
 * @author lulx
 * @email 
 * @date 2022-03-15 21:19:43
 */
public interface AttrService extends IService<AttrEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 获取属性分组的关联的所有属性
     * @param attrGroupId
     * @return
     */
    List<AttrEntity> getAttrsByAttrGroupId(Long attrGroupId);

    /**
     * 获取分类规格参数
     *
     * @param catelogId
     * @param params
     * @param attrType
     * @return
     */
    PageUtils getAttrByCatelogId(Long catelogId, Map<String, Object> params, int attrType);

    /**
     * 查询属性详情
     * @param attrId
     * @return
     */
    AttrInfoVo getAttrInfo(Long attrId);

    void saveAttr(AttrInfoVo attr);

    void updateAttr(AttrInfoVo attr);

    /**
     * 在所有属性即合理，筛选出检索属性
     * @param attrIds
     * @return
     */
    List<Long> selectSearchAttrIds(List<Long> attrIds);

    /**
     * 获取属性分组没有关联的其他属性
     *
     * @param params
     * @param attrGroupId
     * @return
     */
    PageUtils getNoRelationAttr(Map<String, Object> params, Long attrGroupId);
}

