package com.zyj.paocai.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zyj.paocai.utils.PageUtils;
import com.zyj.paocai.entity.SkuSaleAttrValueEntity;

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

