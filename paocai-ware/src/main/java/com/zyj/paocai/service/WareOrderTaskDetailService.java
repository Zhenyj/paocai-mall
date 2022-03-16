package com.zyj.paocai.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zyj.paocai.utils.PageUtils;
import com.zyj.paocai.entity.WareOrderTaskDetailEntity;

import java.util.Map;

/**
 * 库存工作单
 *
 * @author lulx
 * @email 
 * @date 2022-03-15 21:24:53
 */
public interface WareOrderTaskDetailService extends IService<WareOrderTaskDetailEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

