package com.zyj.paocai.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zyj.paocai.common.exception.BizCodeEnum;
import com.zyj.paocai.common.utils.PageUtils;
import com.zyj.paocai.common.utils.Query;
import com.zyj.paocai.common.utils.RRException;
import com.zyj.paocai.product.dao.BrandDao;
import com.zyj.paocai.product.entity.BrandEntity;
import com.zyj.paocai.product.service.BrandService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Map;


@Slf4j
@Service("brandService")
public class BrandServiceImpl extends ServiceImpl<BrandDao, BrandEntity> implements BrandService {

    @Autowired
    BrandDao brandDao;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        QueryWrapper<BrandEntity> wrapper = new QueryWrapper<>();
        String key = (String) params.get("key");
        if (StringUtils.hasText(key)) {
            wrapper.and(w -> {
                w.eq("brand_id", key).or().like("name", key);
            });
        }

        IPage<BrandEntity> page = this.page(
                new Query<BrandEntity>().getPage(params), wrapper
        );

        return new PageUtils(page);
    }

    /**
     * 通过spuId获取品牌信息
     *
     * @param spuId
     * @return
     */
    @Override
    public BrandEntity getBrandBySpuId(Long spuId) {
        BrandEntity brand = brandDao.getBrandBySpuId(spuId);
        if (brand == null) {
            throw new RRException("没有spu相关品牌信息,spuId:" + spuId, BizCodeEnum.PRODUCT_BRAND_EXCEPTION.getCode());
        }
        return brand;
    }

}