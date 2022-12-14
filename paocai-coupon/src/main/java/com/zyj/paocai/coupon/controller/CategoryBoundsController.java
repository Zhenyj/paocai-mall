package com.zyj.paocai.coupon.controller;

import com.zyj.paocai.coupon.entity.CategoryBoundsEntity;
import com.zyj.paocai.coupon.service.CategoryBoundsService;
import com.zyj.paocai.common.utils.PageUtils;
import com.zyj.paocai.common.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Map;


/**
 * 商品分类积分设置
 *
 * @author lulx
 * @email
 * @date 2022-03-15 21:22:50
 */
@RestController
@RequestMapping("coupon/categorybounds")
public class CategoryBoundsController {
    @Autowired
    private CategoryBoundsService categoryBoundsService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    public R<PageUtils> list(@RequestParam Map<String, Object> params) {
        PageUtils page = categoryBoundsService.queryPage(params);
        return R.ok(page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    public R<CategoryBoundsEntity> info(@PathVariable("id") Long id) {
        CategoryBoundsEntity categoryBounds = categoryBoundsService.getById(id);

        return R.ok(categoryBounds);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody CategoryBoundsEntity categoryBounds) {
        categoryBoundsService.save(categoryBounds);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody CategoryBoundsEntity categoryBounds) {
        categoryBoundsService.updateById(categoryBounds);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids) {
        categoryBoundsService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
