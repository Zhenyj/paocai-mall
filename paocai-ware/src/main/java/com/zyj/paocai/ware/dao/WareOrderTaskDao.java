package com.zyj.paocai.ware.dao;

import com.zyj.paocai.ware.entity.WareOrderTaskEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 库存工作单
 * 
 * @author lulx
 * @email 
 * @date 2022-03-15 21:24:53
 */
@Mapper
public interface WareOrderTaskDao extends BaseMapper<WareOrderTaskEntity> {

    /**
     * 通过orderSn获取库存工作单
     * @param orderSn
     * @return
     */
    WareOrderTaskEntity getOrderTaskByOrderSn(@Param("orderSn") String orderSn);
}
