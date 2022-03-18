package com.zyj.paocai.coupon.controller;

import com.zyj.paocai.coupon.entity.CouponEntity;
import com.zyj.paocai.coupon.service.CouponService;
import com.zyj.paocai.common.utils.PageUtils;
import com.zyj.paocai.common.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Map;


/**
 * 优惠券信息
 *
 * @author lulx
 * @email
 * @date 2022-03-15 21:22:50
 */
@RestController
@RequestMapping("coupon/coupon")
public class CouponController {
    @Autowired
    private CouponService couponService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    public R<PageUtils> list(@RequestParam Map<String, Object> params) {
        PageUtils page = couponService.queryPage(params);

        return R.ok(page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    public R<CouponEntity> info(@PathVariable("id") Long id) {
        CouponEntity coupon = couponService.getById(id);

        return R.ok(coupon);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody CouponEntity coupon) {
        couponService.save(coupon);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody CouponEntity coupon) {
        couponService.updateById(coupon);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids) {
        couponService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
