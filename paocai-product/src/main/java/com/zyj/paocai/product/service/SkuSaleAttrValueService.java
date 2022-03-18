package com.zyj.paocai.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zyj.paocai.common.utils.PageUtils;
import com.zyj.paocai.product.entity.SkuSaleAttrValueEntity;

import java.util.Map;

/**
 * sku销售属性&值
 *
 * @author lulx
 * @email 
 * @date 2022-03-15 21:19:43
 */
public interface SkuSaleAttrValueService extends IService<SkuSaleAttrValueEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

