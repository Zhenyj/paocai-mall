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
     * @return
     */
    PageUtils getBaseAttrByCatelogId(Long catelogId, Map<String, Object> params);

    /**
     * 查询属性详情
     * @param attrId
     * @return
     */
    AttrInfoVo getAttrInfo(Long attrId);
}

