package com.zyj.paocai.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zyj.paocai.common.utils.PageUtils;
import com.zyj.paocai.product.entity.AttrEntity;
import com.zyj.paocai.product.entity.AttrGroupEntity;
import com.zyj.paocai.product.entity.vo.AttrGroupWithAttrsVo;
import com.zyj.paocai.product.entity.vo.AttrGroupWithCatelogPathVo;
import com.zyj.paocai.product.entity.vo.SkuItemVo;

import java.util.List;
import java.util.Map;

/**
 * 属性分组
 *
 * @author lulx
 * @email
 * @date 2022-03-15 21:19:43
 */
public interface AttrGroupService extends IService<AttrGroupEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 获取分类属性分组
     *
     * @param catelogId
     * @param params
     * @return
     */
    PageUtils listByCatelogId(Long catelogId, Map<String, Object> params);

    /**
     * 获取属性分组详情
     *
     * @param attrGroupId
     * @return
     */
    AttrGroupWithCatelogPathVo getAttrGroupWithCatelogPathVoById(Long attrGroupId);

    /**
     * 获取属性分组的关联的所有属性
     *
     * @param attrGroupId
     * @return
     */
    List<AttrEntity> getAttrsByAttrGroupId(Long attrGroupId);

    /**
     * 获取分类下所有分组&关联属性
     * @param catelogId
     * @return
     */
    List<AttrGroupWithAttrsVo> getAttrGroupWithAttrs(Long catelogId);

    /**
     * 获取spu规格参数信息
     * @param spuId
     * @param catalogId
     * @return
     */
    List<SkuItemVo.SpuItemAttrGroupVo> getAttrGroupWithAttrsBySpuId(Long spuId, Long catalogId);
}

