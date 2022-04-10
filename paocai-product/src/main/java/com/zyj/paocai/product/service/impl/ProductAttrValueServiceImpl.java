package com.zyj.paocai.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zyj.paocai.common.entity.to.es.SkuEsModel;
import com.zyj.paocai.common.utils.PageUtils;
import com.zyj.paocai.common.utils.Query;
import com.zyj.paocai.product.dao.ProductAttrValueDao;
import com.zyj.paocai.product.entity.ProductAttrValueEntity;
import com.zyj.paocai.product.service.ProductAttrValueService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


@Slf4j
@Service("productAttrValueService")
public class ProductAttrValueServiceImpl extends ServiceImpl<ProductAttrValueDao, ProductAttrValueEntity> implements ProductAttrValueService {

    @Autowired
     ProductAttrValueDao productAttrValueDao;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<ProductAttrValueEntity> page = this.page(
                new Query<ProductAttrValueEntity>().getPage(params),
                new QueryWrapper<ProductAttrValueEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public List<ProductAttrValueEntity> baseAttrListForSpu(Long spuId) {
        return baseMapper.selectList(new QueryWrapper<ProductAttrValueEntity>()
                .eq("spu_id", spuId));
    }

    /**
     * 获取spu规格
     *
     * @param spuId
     * @return
     */
    @Override
    public List<ProductAttrValueEntity> listBaseAttrForSpu(Long spuId) {
        return baseMapper.selectList(new QueryWrapper<ProductAttrValueEntity>()
                .eq("spu_id", spuId));
    }

    @Override
    public void updateBaseAttrForSpu(List<ProductAttrValueEntity> entities, Long spuId) {
        // 删除原来的属性
        baseMapper.deleteBySpuId(spuId);

        // 重新添加属性
        for (ProductAttrValueEntity entity : entities) {
            entity.setSpuId(spuId);
        }
        saveBatch(entities);
    }

    @Override
    public List<SkuEsModel.Attrs> getSearchAttrs(Long spuId) {
        return productAttrValueDao.getSearchAttrs(spuId);
    }

}