package com.zyj.paocai.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zyj.paocai.utils.PageUtils;
import com.zyj.paocai.entity.WareSkuEntity;

import java.util.Map;

/**
 * 商品库存
 *
 * @author lulx
 * @email 
 * @date 2022-03-15 21:24:53
 */
public interface WareSkuService extends IService<WareSkuEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

