package com.zyj.paocai.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zyj.paocai.utils.PageUtils;
import com.zyj.paocai.entity.BrandEntity;

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
}

