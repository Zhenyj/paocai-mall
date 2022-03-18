package com.zyj.paocai.coupon.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zyj.paocai.coupon.entity.HomeSubjectSpuEntity;
import com.zyj.paocai.common.utils.PageUtils;

import java.util.Map;

/**
 * 专题商品
 *
 * @author lulx
 * @email 
 * @date 2022-03-15 21:22:50
 */
public interface HomeSubjectSpuService extends IService<HomeSubjectSpuEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

