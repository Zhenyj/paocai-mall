package com.zyj.paocai.order.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zyj.paocai.common.constant.Constant;
import com.zyj.paocai.common.entity.vo.CartSkuItem;
import com.zyj.paocai.common.entity.vo.MemberRespVo;
import com.zyj.paocai.common.entity.vo.OrderStatusNumsVo;
import com.zyj.paocai.common.entity.vo.SkuHasStockVo;
import com.zyj.paocai.common.exception.BizCodeEnum;
import com.zyj.paocai.common.utils.PageUtils;
import com.zyj.paocai.common.utils.Query;
import com.zyj.paocai.common.utils.R;
import com.zyj.paocai.common.utils.RRException;
import com.zyj.paocai.order.dao.OrderDao;
import com.zyj.paocai.order.entity.OrderEntity;
import com.zyj.paocai.order.entity.vo.OrderConfirmVo;
import com.zyj.paocai.order.feign.ProductFeignService;
import com.zyj.paocai.order.feign.WareFeignService;
import com.zyj.paocai.order.interceptor.LoginInfoInterceptor;
import com.zyj.paocai.order.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;


@Slf4j
@Service("orderService")
public class OrderServiceImpl extends ServiceImpl<OrderDao, OrderEntity> implements OrderService {

    @Autowired
    OrderDao orderDao;

    @Autowired
    ProductFeignService productFeignService;

    @Autowired
    WareFeignService wareFeignService;

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
     * @return
     */
    @Override
    public OrderStatusNumsVo getOrderStatusNumsInfo() {
        MemberRespVo member = LoginInfoInterceptor.loginInfo.get();
        OrderStatusNumsVo vo = orderDao.getOrderStatusNumsInfo(member.getId());
        // TODO 获取用户购物车商品数量(总数)
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
        R<List<CartSkuItem>> r = productFeignService.getSkuItems(skuIds);
        if(!Constant.SUCCESS_CODE.equals(r.getCode())){
            throw new RRException(r.getMsg(),r.getCode());
        }
        List<CartSkuItem> cartSkuItems = r.getData();
        // 获取库存信息
        R<List<SkuHasStockVo>> skuHasStockBatchResult = wareFeignService.getSkuHasStockBatch(skuIds);
        if(!Constant.SUCCESS_CODE.equals(skuHasStockBatchResult.getCode())){
            throw new RRException(skuHasStockBatchResult.getMsg(),skuHasStockBatchResult.getCode());
        }
        List<SkuHasStockVo> skuHasStockVos = skuHasStockBatchResult.getData();
        if(!CollectionUtils.isEmpty(skuHasStockVos) || skuHasStockVos.size() != skuIds.size()){
             throw new RRException(BizCodeEnum.PRODUCT_WARE_EXCEPTION.getMsg(), BizCodeEnum.PRODUCT_WARE_EXCEPTION.getCode());
        }
        // 转换为skuId为键的map集合
        Map<Long, SkuHasStockVo> hasStockVoMap = skuHasStockVos.stream()
                .collect(Collectors.toMap(SkuHasStockVo::getSkuId, Function.identity()));
        for (CartSkuItem cartSkuItem : cartSkuItems) {
            cartSkuItem.setHasStock(hasStockVoMap.get(cartSkuItem.getSkuId()).getHasStock());
        }

        return null;
    }

}