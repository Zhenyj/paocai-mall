package com.zyj.paocai.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zyj.paocai.common.utils.PageUtils;
import com.zyj.paocai.common.utils.Query;
import com.zyj.paocai.product.dao.SkuSaleAttrValueDao;
import com.zyj.paocai.product.entity.SkuSaleAttrValueEntity;
import com.zyj.paocai.product.entity.vo.SkuItemVo;
import com.zyj.paocai.product.service.SkuSaleAttrValueService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


@Slf4j
@Service("skuSaleAttrValueService")
public class SkuSaleAttrValueServiceImpl extends ServiceImpl<SkuSaleAttrValueDao, SkuSaleAttrValueEntity> implements SkuSaleAttrValueService {

    @Autowired
    SkuSaleAttrValueDao skuSaleAttrValueDao;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SkuSaleAttrValueEntity> page = this.page(
                new Query<SkuSaleAttrValueEntity>().getPage(params),
                new QueryWrapper<SkuSaleAttrValueEntity>()
        );

        return new PageUtils(page);
    }

    /**
     * 获取spu基本属性
     * @param spuId
     * @return
     */
    @Override
    public List<SkuItemVo.SkuItemSaleAttrVo> getSaleAttrsBySpuId(Long spuId) {
        List<SkuItemVo.SkuItemSaleAttrVo> saleAttrs = baseMapper.getSaleAttrsBySpuId(spuId);
        return saleAttrs;
    }

    /**
     * 获取sku销售属性
     * @param skuId
     * @return
     */
    @Override
    public List<SkuSaleAttrValueEntity> getSaleAttrsBySkuId(Long skuId) {
        List<SkuSaleAttrValueEntity> saleAttrs = baseMapper.selectList(new QueryWrapper<SkuSaleAttrValueEntity>()
                .eq("sku_id",skuId));
        return saleAttrs;
    }
}