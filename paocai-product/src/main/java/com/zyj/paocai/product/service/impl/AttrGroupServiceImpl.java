package com.zyj.paocai.product.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zyj.paocai.common.utils.PageUtils;
import com.zyj.paocai.common.utils.Query;
import com.zyj.paocai.product.dao.AttrGroupDao;
import com.zyj.paocai.product.entity.AttrEntity;
import com.zyj.paocai.product.entity.AttrGroupEntity;
import com.zyj.paocai.product.entity.vo.AttrGroupWithAttrsVo;
import com.zyj.paocai.product.entity.vo.AttrGroupWithCatelogPathVo;
import com.zyj.paocai.product.entity.vo.SkuItemVo;
import com.zyj.paocai.product.service.AttrAttrgroupRelationService;
import com.zyj.paocai.product.service.AttrGroupService;
import com.zyj.paocai.product.service.AttrService;
import com.zyj.paocai.product.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;

@Slf4j
@Service("attrGroupService")
public class AttrGroupServiceImpl extends ServiceImpl<AttrGroupDao, AttrGroupEntity> implements AttrGroupService {

    @Autowired
    AttrGroupDao attrGroupDao;

    @Autowired
    CategoryService categoryService;

    @Autowired
    AttrAttrgroupRelationService attrAttrgroupRelationService;

    @Autowired
    AttrService attrService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<AttrGroupEntity> page = this.page(
                new Query<AttrGroupEntity>().getPage(params),
                new QueryWrapper<AttrGroupEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public PageUtils listByCatelogId(Long catelogId, Map<String, Object> params) {
        QueryWrapper<AttrGroupEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("catelog_id", catelogId);

        String key = (String) params.get("key");
        if (StringUtils.hasText(key)) {
            wrapper.and((w) -> {
                w.eq("attr_group_id", key).or().like("attr_group_name", key);
            });
        }
        IPage<AttrGroupEntity> page = null;
        if (catelogId == 0) {
            page = this.page(new Query<AttrGroupEntity>().getPage(params),
                    wrapper);
        } else {
            page = this.page(new Query<AttrGroupEntity>().getPage(params), wrapper.eq("catelog_id", catelogId));
        }

        return new PageUtils(page);
    }

    @Override
    public AttrGroupWithCatelogPathVo getAttrGroupWithCatelogPathVoById(Long attrGroupId) {
        AttrGroupEntity attrGroupEntity = this.getById(attrGroupId);
        if (attrGroupEntity == null) {
            throw new RuntimeException("不存在" + attrGroupId + "对应的属性分组");
        }
        AttrGroupWithCatelogPathVo vo = new AttrGroupWithCatelogPathVo();
        vo.setAttrGroupId(attrGroupEntity.getAttrGroupId());
        vo.setAttrGroupName(attrGroupEntity.getAttrGroupName());
        vo.setCatelogId(attrGroupEntity.getCatelogId());
        vo.setDescript(attrGroupEntity.getDescript());
        vo.setIcon(attrGroupEntity.getIcon());
        vo.setSort(attrGroupEntity.getSort());
        vo.setCatelogPath(categoryService.getCatelogPath(attrGroupEntity.getCatelogId()));

        return vo;
    }

    @Override
    public List<AttrEntity> getAttrsByAttrGroupId(Long attrGroupId) {
        log.info("获取属性分组id:{}的相关属性", attrGroupId);
        List<AttrEntity> attrEntities = attrService.getAttrsByAttrGroupId(attrGroupId);
        if (CollectionUtils.isEmpty(attrEntities)) {
            log.info("暂无属性分组id:{}的相关属性", attrGroupId);
        }
        return attrEntities;
    }

    /**
     * 获取分类下所有分组&关联属性
     * 通过sql组成的JSON字符串转为List
     * @param catelogId
     * @return
     */
    @Override
    public List<AttrGroupWithAttrsVo> getAttrGroupWithAttrs(Long catelogId) {
        List<AttrGroupWithAttrsVo> vos = attrGroupDao.getAttrGroupWithAttrs(catelogId);
        if(vos != null && vos.size() > 0){
            for (AttrGroupWithAttrsVo vo : vos) {
                // 通过sql组成的JSON字符串转为List
                vo.setAttrs(JSON.parseObject(vo.getAttrString(), new TypeReference<List<AttrEntity>>(){}));
                vo.setAttrString("");
            }
        }
        return vos;
    }

    @Override
    public List<SkuItemVo.SpuItemAttrGroupVo> getAttrGroupWithAttrsBySpuId(Long spuId, Long catalogId) {
        // 1、查询当前spu对应的所有属性的分组信息以及当前分组下的所有属性对应的值
        List<SkuItemVo.SpuItemAttrGroupVo> vo = attrGroupDao.getAttrGroupWithAttrsBySpuId(spuId, catalogId);
        return vo;
    }

}