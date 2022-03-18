package com.zyj.paocai.coupon.service.impl;

import com.zyj.paocai.coupon.dao.SkuBoundsDao;
import com.zyj.paocai.coupon.entity.SkuBoundsEntity;
import com.zyj.paocai.coupon.service.SkuBoundsService;
import com.zyj.paocai.common.utils.PageUtils;
import com.zyj.paocai.common.utils.Query;
import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;


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

}