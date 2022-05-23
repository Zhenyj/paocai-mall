package com.zyj.paocai.order.controller;

import com.alipay.api.AlipayApiException;
import com.zyj.paocai.common.entity.vo.OrderStatusNumsVo;
import com.zyj.paocai.common.utils.PageUtils;
import com.zyj.paocai.common.utils.R;
import com.zyj.paocai.order.entity.OrderEntity;
import com.zyj.paocai.order.entity.vo.CartItemBaseVo;
import com.zyj.paocai.order.entity.vo.OrderConfirmVo;
import com.zyj.paocai.order.entity.vo.OrderSubmitRespVo;
import com.zyj.paocai.order.entity.vo.OrderSubmitVo;
import com.zyj.paocai.order.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;


/**
 * 订单
 *
 * @author lulx
 * @email
 * @date 2022-03-15 21:12:53
 */
@RestController
@RequestMapping("order/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    /**
     * 提交订单、创建订单
     *
     * @param orderSubmitVo
     * @return
     */
    @PostMapping("/submit_order")
    public R<OrderSubmitRespVo> submitOrder(@RequestBody OrderSubmitVo orderSubmitVo) throws AlipayApiException {
        OrderSubmitRespVo vo = orderService.submitOrder(orderSubmitVo);
        return R.ok(vo);
    }

    /**
     * 结算并返回订单确认信息
     *
     * @param vos
     * @return
     */
    @PostMapping("/toTrade")
    public R<OrderConfirmVo> toTrade(@RequestBody List<CartItemBaseVo> vos) throws ExecutionException, InterruptedException {
        OrderConfirmVo orderConfirmVo = orderService.toTrade(vos);
        return R.ok(orderConfirmVo);
    }

    /**
     * 立即购买单个商品
     *
     * @param vo
     * @return
     */
    @PostMapping("/toTradeOne")
    public R<OrderConfirmVo> toTradeOne(@RequestBody CartItemBaseVo vo) throws ExecutionException, InterruptedException {
        OrderConfirmVo orderConfirmVo = orderService.toTradeOne(vo);
        return R.ok(orderConfirmVo);
    }

    /**
     * 获取用户订单状态信息
     *
     * @return
     */
    @GetMapping("/order_status_num")
    public R<OrderStatusNumsVo> getOrderStatusNumsInfo() {
        OrderStatusNumsVo vo = orderService.getOrderStatusNumsInfo();
        return R.ok(vo);
    }

    /**
     * 列表
     */
    @RequestMapping("/list")
    public R<PageUtils> list(@RequestParam Map<String, Object> params) {
        PageUtils page = orderService.queryPage(params);

        return R.ok(page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id) {
        OrderEntity order = orderService.getById(id);

        return R.ok(order);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody OrderEntity order) {
        orderService.save(order);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody OrderEntity order) {
        orderService.updateById(order);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids) {
        orderService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
