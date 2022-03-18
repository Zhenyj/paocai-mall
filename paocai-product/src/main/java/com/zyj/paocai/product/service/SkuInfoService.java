package com.zyj.paocai.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zyj.paocai.common.utils.PageUtils;
import com.zyj.paocai.product.entity.SkuInfoEntity;

import java.util.Map;

/**
 * sku信息
 *
 * @author lulx
 * @email 
 * @date 2022-03-15 21:19:43
 */
public interface SkuInfoService extends IService<SkuInfoEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 根据自定义查询参查询sku信息
     * @param params
     * @return
     */
    PageUtils queryPageByCondition(Map<String, Object> params);
}

