package com.zyj.paocai.ware.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zyj.paocai.common.utils.PageUtils;
import com.zyj.paocai.ware.entity.WareOrderTaskEntity;

import java.util.Map;

/**
 * 库存工作单
 *
 * @author lulx
 * @email 
 * @date 2022-03-15 21:24:53
 */
public interface WareOrderTaskService extends IService<WareOrderTaskEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 获取库存工作单
     * @param orderSn
     * @return
     */
    WareOrderTaskEntity getOrderTaskByOrderSn(String orderSn);
}

