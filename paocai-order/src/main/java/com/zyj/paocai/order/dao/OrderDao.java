package com.zyj.paocai.order.dao;

import com.zyj.paocai.common.entity.vo.OrderStatusNumsVo;
import com.zyj.paocai.order.entity.OrderEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zyj.paocai.order.entity.vo.OrderVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

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
     *
     * @param memberId
     * @return
     */
    OrderStatusNumsVo getOrderStatusNumsInfo(@Param("memberId") Long memberId);

    /**
     * 订单列表
     *
     * @param memberId
     * @param offset
     * @param limit
     * @param key
     * @param status
     * @return
     */
    List<OrderVo> getOrderList(@Param("memberId") Long memberId, @Param("offset") Integer offset,
                               @Param("limit") Integer limit, @Param("key") String key, @Param("status") Integer status);

    /**
     * 相关订单总数
     *
     * @param memberId
     * @param key
     * @param status
     * @return
     */
    Integer getOrderListTotal(@Param("memberId") Long memberId, @Param("key") String key, @Param("status") Integer status);
}
