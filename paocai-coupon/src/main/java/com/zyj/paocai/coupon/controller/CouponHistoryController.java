package com.zyj.paocai.coupon.controller;

import com.zyj.paocai.coupon.entity.CouponHistoryEntity;
import com.zyj.paocai.coupon.service.CouponHistoryService;
import com.zyj.paocai.common.utils.PageUtils;
import com.zyj.paocai.common.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Map;


/**
 * 优惠券领取历史记录
 *
 * @author lulx
 * @email
 * @date 2022-03-15 21:22:50
 */
@RestController
@RequestMapping("coupon/couponhistory")
public class CouponHistoryController {
    @Autowired
    private CouponHistoryService couponHistoryService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    public R<PageUtils> list(@RequestParam Map<String, Object> params) {
        PageUtils page = couponHistoryService.queryPage(params);

        return R.ok(page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    public R<CouponHistoryEntity> info (@PathVariable("id") Long id) {
         CouponHistoryEntity couponHistory = couponHistoryService.getById(id);

        return R.ok(couponHistory);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody CouponHistoryEntity couponHistory) {
        couponHistoryService.save(couponHistory);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody CouponHistoryEntity couponHistory) {
        couponHistoryService.updateById(couponHistory);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids) {
        couponHistoryService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
