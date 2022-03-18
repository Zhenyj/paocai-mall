package com.zyj.paocai.ware.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zyj.paocai.common.utils.PageUtils;
import com.zyj.paocai.ware.entity.PurchaseDetailEntity;

import java.util.Map;

/**
 * 
 *
 * @author lulx
 * @email 
 * @date 2022-03-15 21:24:53
 */
public interface PurchaseDetailService extends IService<PurchaseDetailEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

