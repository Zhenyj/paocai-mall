package com.zyj.paocai.product.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zyj.paocai.common.exception.BizCodeEnum;
import com.zyj.paocai.common.utils.PageUtils;
import com.zyj.paocai.common.utils.Query;
import com.zyj.paocai.common.utils.RRException;
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
            throw new RRException("?????????" + attrGroupId + "?????????????????????", BizCodeEnum.PRODUCT_ATTR_GROUP_EXCEPTION.getCode());
        }
        AttrGroupWithCatelogPathVo vo = new AttrGroupWithCatelogPathVo();
        vo.setAttrGroupId(attrGroupEntity.getAttrGroupId());
        vo.setAttrGroupName(attrGroupEntity.getAttrGroupName());
        vo.setCatelogId(attrGroupEntity.getCatelogId());
        vo.setDescript(attrGroupEntity.getDescript());
        vo.setIcon(attrGroupEntity.getIcon());
        vo.setSort(attrGroupEntity.getSort());
        vo.setCatelogPath(categoryService.getCatalogPath(attrGroupEntity.getCatelogId()));

        return vo;
    }

    @Override
    public List<AttrEntity> getAttrsByAttrGroupId(Long attrGroupId) {
        log.info("??????????????????id:{}???????????????", attrGroupId);
        List<AttrEntity> attrEntities = attrService.getAttrsByAttrGroupId(attrGroupId);
        if (CollectionUtils.isEmpty(attrEntities)) {
            log.info("??????????????????id:{}???????????????", attrGroupId);
        }
        return attrEntities;
    }

    /**
     * ???????????????????????????&????????????
     * ??????sql?????????JSON???????????????List
     * @param catelogId
     * @return
     */
    @Override
    public List<AttrGroupWithAttrsVo> getAttrGroupWithAttrs(Long catelogId) {
        List<AttrGroupWithAttrsVo> vos = attrGroupDao.getAttrGroupWithAttrs(catelogId);
        if(vos != null && vos.size() > 0){
            for (AttrGroupWithAttrsVo vo : vos) {
                // ??????sql?????????JSON???????????????List
                vo.setAttrs(JSON.parseObject(vo.getAttrString(), new TypeReference<List<AttrEntity>>(){}));
                vo.setAttrString("");
            }
        }
        return vos;
    }

    @Override
    public List<SkuItemVo.SpuItemAttrGroupVo> getAttrGroupWithAttrsBySpuId(Long spuId, Long catalogId) {
        // 1???????????????spu????????????????????????????????????????????????????????????????????????????????????
        List<SkuItemVo.SpuItemAttrGroupVo> vo = attrGroupDao.getAttrGroupWithAttrsBySpuId(spuId, catalogId);
        return vo;
    }

}