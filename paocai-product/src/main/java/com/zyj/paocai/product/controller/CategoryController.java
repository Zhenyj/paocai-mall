package com.zyj.paocai.product.controller;

import com.zyj.paocai.product.entity.CategoryEntity;
import com.zyj.paocai.product.service.CategoryService;
import com.zyj.paocai.common.utils.PageUtils;
import com.zyj.paocai.common.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;


/**
 * 商品三级分类
 *
 * @author lulx
 * @email
 * @date 2022-03-15 21:19:43
 */
@RestController
@RequestMapping("product/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @GetMapping("/list/tree")
    public R<List<CategoryEntity>> listTree() {
        List<CategoryEntity> list = categoryService.listTree();
        return R.ok(list);
    }

    /**
     * 列表
     */
    @RequestMapping("/list")
    public R<PageUtils> list(@RequestParam Map<String, Object> params) {
        PageUtils page = categoryService.queryPage(params);

        return R.ok(page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{catId}")
    public R info(@PathVariable("catId") Long catId) {
        CategoryEntity category = categoryService.getById(catId);

        return R.ok(category);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody CategoryEntity category) {
        categoryService.save(category);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody CategoryEntity category) {
        categoryService.updateById(category);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] catIds) {
        categoryService.removeByIds(Arrays.asList(catIds));

        return R.ok();
    }

}
