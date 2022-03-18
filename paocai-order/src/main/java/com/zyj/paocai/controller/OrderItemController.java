package com.zyj.paocai.controller;

import com.zyj.paocai.entity.OrderItemEntity;
import com.zyj.paocai.service.OrderItemService;
import com.zyj.paocai.utils.PageUtils;
import com.zyj.paocai.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Map;



/**
 * 订单项信息
 *
 * @author lulx
 * @email 
 * @date 2022-03-15 21:12:53
 */
@RestController
@RequestMapping("order/orderitem")
public class OrderItemController {
    @Autowired
    private OrderItemService orderItemService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    public R<PageUtils> list(@RequestParam Map<String, Object> params){
        PageUtils page = orderItemService.queryPage(params);

        return R.ok(page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    public R<OrderItemEntity> info(@PathVariable("id") Long id){
		OrderItemEntity orderItem = orderItemService.getById(id);

        return R.ok(orderItem);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody OrderItemEntity orderItem){
		orderItemService.save(orderItem);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody OrderItemEntity orderItem){
		orderItemService.updateById(orderItem);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids){
		orderItemService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
