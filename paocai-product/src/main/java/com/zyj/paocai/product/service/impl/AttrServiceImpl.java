package com.zyj.paocai.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zyj.paocai.common.utils.PageUtils;
import com.zyj.paocai.common.utils.Query;
import com.zyj.paocai.common.constant.ProductConstant;
import com.zyj.paocai.product.dao.AttrAttrgroupRelationDao;
import com.zyj.paocai.product.dao.AttrDao;
import com.zyj.paocai.product.entity.AttrAttrgroupRelationEntity;
import com.zyj.paocai.product.entity.AttrEntity;
import com.zyj.paocai.product.entity.vo.AttrBaseVo;
import com.zyj.paocai.product.entity.vo.AttrInfoVo;
import com.zyj.paocai.product.service.AttrService;
import com.zyj.paocai.product.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;


@Slf4j
@Service("attrService")
public class AttrServiceImpl extends ServiceImpl<AttrDao, AttrEntity> implements AttrService {

    @Autowired
    AttrDao attrDao;

    @Autowired
    CategoryService categoryService;

    @Autowired
    AttrAttrgroupRelationDao attrAttrgroupRelationDao;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<AttrEntity> page = this.page(
                new Query<AttrEntity>().getPage(params),
                new QueryWrapper<AttrEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public List<AttrEntity> getAttrsByAttrGroupId(Long attrGroupId) {
        return attrDao.getAttrsByAttrGroupId(attrGroupId);
    }

    @Override
    public PageUtils getAttrByCatelogId(Long catelogId, Map<String, Object> params, int attrType) {
        String key = (String) params.get("key");
        List<AttrBaseVo> attrs = attrDao.getAttrByCatelogId(catelogId, key, attrType);
        return new PageUtils(attrs, params);
    }

    @Override
    public AttrInfoVo getAttrInfo(Long attrId) {
        log.info("获取attrId:{}的属性详情", attrId);
        AttrInfoVo attrInfoVo = attrDao.getAttrInfo(attrId);
        if (attrInfoVo == null) {
            return null;
        }
        attrInfoVo.setCatelogPath(categoryService.getCatelogPath(attrInfoVo.getCatelogId()));
        return attrInfoVo;
    }

    @Transactional
    @Override
    public void saveAttr(AttrInfoVo attr) {
        AttrEntity attrEntity = new AttrEntity();
        // Spring提供的属性复制
        BeanUtils.copyProperties(attr, attrEntity);
        // 1、保存基本数据
        this.save(attrEntity);
        // 2、保存关联关系,基本属性才需要保存关联关系，销售属性不需要
        if (attr.getAttrType() == ProductConstant.AttrEnum.ATTR_TYPE_BASE.getCode() && attr.getAttrGroupId() != null) {
            AttrAttrgroupRelationEntity relationEntity = new AttrAttrgroupRelationEntity();
            relationEntity.setAttrGroupId(attr.getAttrGroupId());
            relationEntity.setAttrId(attrEntity.getAttrId());
            attrAttrgroupRelationDao.insert(relationEntity);
        }
    }

    @Transactional
    @Override
    public void updateAttr(AttrInfoVo attr) {
        AttrEntity attrEntity = new AttrEntity();
        BeanUtils.copyProperties(attr, attrEntity);
        this.updateById(attrEntity);

        // 修改分组关联
        if (attr.getAttrType() == ProductConstant.AttrEnum.ATTR_TYPE_BASE.getCode()) {
            AttrAttrgroupRelationEntity relationEntity = new AttrAttrgroupRelationEntity();
            relationEntity.setAttrGroupId(attr.getAttrGroupId());
            relationEntity.setAttrId(attr.getAttrId());
            Integer count = attrAttrgroupRelationDao.selectCount(new QueryWrapper<AttrAttrgroupRelationEntity>().eq("attr_id",
                    attr.getAttrId()));
            if (count > 0) {
                // 修改分组关联
                attrAttrgroupRelationDao.update(relationEntity,
                        new UpdateWrapper<AttrAttrgroupRelationEntity>().eq("attr_id", attr.getAttrId()));
            } else {
                // 新增分组关联
                attrAttrgroupRelationDao.insert(relationEntity);
            }
        }
    }

}