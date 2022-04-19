package com.zyj.paocai.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zyj.paocai.common.utils.PageUtils;
import com.zyj.paocai.product.entity.BrandEntity;

import java.util.Map;

/**
 * 品牌
 *
 * @author lulx
 * @email 
 * @date 2022-03-15 21:19:43
 */
public interface BrandService extends IService<BrandEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 通过spuId获取品牌信息
     * @param spuId
     * @return
     */
    BrandEntity getBrandBySpuId(Long spuId);
}

