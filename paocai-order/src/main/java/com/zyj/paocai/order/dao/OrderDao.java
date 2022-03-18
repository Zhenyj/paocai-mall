package com.zyj.paocai.order.dao;

import com.zyj.paocai.order.entity.OrderEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 订单
 * 
 * @author lulx
 * @email 
 * @date 2022-03-15 21:12:53
 */
@Mapper
public interface OrderDao extends BaseMapper<OrderEntity> {
	
}
