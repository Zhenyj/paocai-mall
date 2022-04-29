package com.zyj.paocai.order.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zyj.paocai.common.entity.vo.CartSkuItem;
import com.zyj.paocai.common.entity.vo.MemberRespVo;
import com.zyj.paocai.common.entity.vo.OrderStatusNumsVo;
import com.zyj.paocai.common.utils.PageUtils;
import com.zyj.paocai.common.utils.Query;
import com.zyj.paocai.order.dao.OrderDao;
import com.zyj.paocai.order.entity.OrderEntity;
import com.zyj.paocai.order.entity.vo.OrderConfirmVo;
import com.zyj.paocai.order.interceptor.LoginInfoInterceptor;
import com.zyj.paocai.order.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Slf4j
@Service("orderService")
public class OrderServiceImpl extends ServiceImpl<OrderDao, OrderEntity> implements OrderService {

    @Autowired
    OrderDao orderDao;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<OrderEntity> page = this.page(
                new Query<OrderEntity>().getPage(params),
                new QueryWrapper<OrderEntity>()
        );

        return new PageUtils(page);
    }

    /**
     * 获取用户订单状态信息
     * @param memberId
     * @return
     */
    @Override
    public OrderStatusNumsVo getOrderStatusNumsInfo(Long memberId) {
        OrderStatusNumsVo vo = orderDao.getOrderStatusNumsInfo(memberId);
        return vo;
    }

    /**
     * 结算并返回订单确认信息
     * @param skuItems
     * @return
     */
    @Override
    public OrderConfirmVo toTrade(List<CartSkuItem> skuItems) {
        OrderConfirmVo vo = new OrderConfirmVo();
        MemberRespVo member = LoginInfoInterceptor.loginInfo.get();
        List<Long> skuIds = skuItems.stream().map(CartSkuItem::getSkuId).collect(Collectors.toList());
        // 获取最新的商品信息


        // 获取库存信息
        return null;
    }

}