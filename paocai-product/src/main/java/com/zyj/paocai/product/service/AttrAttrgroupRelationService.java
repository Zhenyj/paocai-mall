package com.zyj.paocai.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zyj.paocai.common.utils.PageUtils;
import com.zyj.paocai.product.entity.AttrAttrgroupRelationEntity;
import com.zyj.paocai.product.entity.vo.AttrGroupRelationVo;

import java.util.List;
import java.util.Map;

/**
 * 属性&属性分组关联
 *
 * @author lulx
 * @email 
 * @date 2022-03-15 21:19:43
 */
public interface AttrAttrgroupRelationService extends IService<AttrAttrgroupRelationEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 删除属性与分组的关联关系
     * @param relations
     */
    void deleteRelation(List<AttrGroupRelationVo> relations);
}

