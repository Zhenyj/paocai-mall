package com.zyj.paocai.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zyj.paocai.common.utils.PageUtils;
import com.zyj.paocai.product.entity.SpuInfoEntity;

import java.util.Map;

/**
 * spu信息
 *
 * @author lulx
 * @email 
 * @date 2022-03-15 21:19:43
 */
public interface SpuInfoService extends IService<SpuInfoEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

