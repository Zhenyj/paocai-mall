package com.zyj.paocai.order.dao;

import com.zyj.paocai.order.entity.OrderItemEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 订单项信息
 * 
 * @author lulx
 * @email 
 * @date 2022-03-15 21:12:53
 */
@Mapper
public interface OrderItemDao extends BaseMapper<OrderItemEntity> {
	
}
