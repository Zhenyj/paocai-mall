package com.zyj.paocai.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zyj.paocai.common.utils.PageUtils;
import com.zyj.paocai.product.entity.AttrEntity;

import java.util.Map;

/**
 * 商品属性
 *
 * @author lulx
 * @email 
 * @date 2022-03-15 21:19:43
 */
public interface AttrService extends IService<AttrEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

