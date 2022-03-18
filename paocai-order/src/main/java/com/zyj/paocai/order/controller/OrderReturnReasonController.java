package com.zyj.paocai.order.controller;

import com.zyj.paocai.order.entity.OrderReturnReasonEntity;
import com.zyj.paocai.order.service.OrderReturnReasonService;
import com.zyj.paocai.common.utils.PageUtils;
import com.zyj.paocai.common.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Map;


/**
 * 退货原因
 *
 * @author lulx
 * @email
 * @date 2022-03-15 21:12:53
 */
@RestController
@RequestMapping("order/orderreturnreason")
public class OrderReturnReasonController {
    @Autowired
    private OrderReturnReasonService orderReturnReasonService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    public R<PageUtils> list(@RequestParam Map<String, Object> params) {
        PageUtils page = orderReturnReasonService.queryPage(params);

        return R.ok(page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    public R<OrderReturnReasonEntity> info (@PathVariable("id") Long id) {
        OrderReturnReasonEntity orderReturnReason = orderReturnReasonService.getById(id);

        return R.ok(orderReturnReason);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody OrderReturnReasonEntity orderReturnReason) {
        orderReturnReasonService.save(orderReturnReason);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody OrderReturnReasonEntity orderReturnReason) {
        orderReturnReasonService.updateById(orderReturnReason);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids) {
        orderReturnReasonService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
