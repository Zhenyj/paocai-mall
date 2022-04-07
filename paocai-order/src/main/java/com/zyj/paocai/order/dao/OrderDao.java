package com.zyj.paocai.order.dao;

import com.zyj.paocai.common.entity.vo.OrderStatusNumsVo;
import com.zyj.paocai.order.entity.OrderEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 订单
 * 
 * @author lulx
 * @email 
 * @date 2022-03-15 21:12:53
 */
@Mapper
public interface OrderDao extends BaseMapper<OrderEntity> {

    /**
     * 获取用户订单状态信息
     * @param memberId
     * @return
     */
    OrderStatusNumsVo getOrderStatusNumsInfo(@Param("memberId") Long memberId);
}
