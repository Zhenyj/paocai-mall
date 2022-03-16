package com.zyj.paocai.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zyj.paocai.utils.PageUtils;
import com.zyj.paocai.entity.RefundInfoEntity;

import java.util.Map;

/**
 * 退款信息
 *
 * @author lulx
 * @email 
 * @date 2022-03-15 21:12:53
 */
public interface RefundInfoService extends IService<RefundInfoEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

