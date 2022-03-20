package com.zyj.paocai.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zyj.paocai.common.utils.PageUtils;
import com.zyj.paocai.common.utils.Query;
import com.zyj.paocai.product.dao.AttrDao;
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
    public PageUtils getBaseAttrByCatelogId(Long catelogId, Map<String, Object> params) {
        String key = (String) params.get("key");
        List<AttrBaseVo> attrs = attrDao.getBaseAttrByCatelogId(catelogId, key);
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

}