package com.zyj.paocai.order.controller;

import java.util.Arrays;
import java.util.Map;


import com.zyj.paocai.order.entity.OrderReturnApplyEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.zyj.paocai.order.service.OrderReturnApplyService;
import com.zyj.paocai.common.utils.PageUtils;
import com.zyj.paocai.common.utils.R;



/**
 * 订单退货申请
 *
 * @author lulx
 * @email 
 * @date 2022-03-15 21:12:53
 */
@RestController
@RequestMapping("order/orderreturnapply")
public class OrderReturnApplyController {
    @Autowired
    private OrderReturnApplyService orderReturnApplyService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    public R<PageUtils> list(@RequestParam Map<String, Object> params){
        PageUtils page = orderReturnApplyService.queryPage(params);

        return R.ok(page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id){
		OrderReturnApplyEntity orderReturnApply = orderReturnApplyService.getById(id);

        return R.ok(orderReturnApply);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody OrderReturnApplyEntity orderReturnApply){
		orderReturnApplyService.save(orderReturnApply);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody OrderReturnApplyEntity orderReturnApply){
		orderReturnApplyService.updateById(orderReturnApply);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids){
		orderReturnApplyService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
