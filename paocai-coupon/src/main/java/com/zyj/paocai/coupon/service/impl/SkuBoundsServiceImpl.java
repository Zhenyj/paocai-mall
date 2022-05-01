package com.zyj.paocai.coupon.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zyj.paocai.common.utils.PageUtils;
import com.zyj.paocai.common.utils.Query;
import com.zyj.paocai.coupon.dao.SkuBoundsDao;
import com.zyj.paocai.coupon.entity.SkuBoundsEntity;
import com.zyj.paocai.coupon.service.SkuBoundsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Map;


@Slf4j
@Service("skuBoundsService")
public class SkuBoundsServiceImpl extends ServiceImpl<SkuBoundsDao, SkuBoundsEntity> implements SkuBoundsService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SkuBoundsEntity> page = this.page(
                new Query<SkuBoundsEntity>().getPage(params),
                new QueryWrapper<SkuBoundsEntity>()
        );

        return new PageUtils(page);
    }

    /**
     * 根据skuId获取sku积分信息
     *
     * @param skuId
     * @return
     */
    @Override
    public SkuBoundsEntity getBySkuId(Long skuId) {
        return baseMapper.selectOne(new QueryWrapper<SkuBoundsEntity>().eq("sku_id", skuId));
    }

    /**
     * 获取商品sku积分信息
     *
     * @param skuId
     * @return
     */
    @Override
    public SkuBoundsEntity getBoundsBySkuId(Long skuId) {
        SkuBoundsEntity skuBoundsEntity = baseMapper.selectOne(new QueryWrapper<SkuBoundsEntity>().eq("sku_id", skuId));
        if (skuBoundsEntity != null) {
            if(skuBoundsEntity.getBuyBounds() == null){
                skuBoundsEntity.setBuyBounds(new BigDecimal(0));
            }
            if(skuBoundsEntity.getGrowBounds() == null){
                skuBoundsEntity.setGrowBounds(new BigDecimal(0));
            }
        }
        return skuBoundsEntity;
    }

}