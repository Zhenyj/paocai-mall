package com.zyj.paocai.product.controller;

import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.zyj.paocai.common.entity.vo.CatalogBaseVo;
import com.zyj.paocai.product.entity.CategoryEntity;
import com.zyj.paocai.product.service.CategoryService;
import com.zyj.paocai.common.utils.PageUtils;
import com.zyj.paocai.common.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


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

    /**
     * 获取同级分类
     *
     * @param catalogId
     * @return
     */
    @RequestMapping("/same_level_category")
    R<List<CatalogBaseVo>> getSameLevelCategory(@RequestParam("catalogId") Long catalogId) {
        List<CategoryEntity> entities = categoryService.getSameLevelCategory(catalogId);
        List<CatalogBaseVo> list = entities.stream().map(entity -> {
            CatalogBaseVo vo = new CatalogBaseVo();
            vo.setCatalogId(entity.getCatId());
            vo.setCatalogName(entity.getName());
            return vo;
        }).collect(Collectors.toList());
        return R.ok(list);
    }

    @RequestMapping("/catalogs")
    public R<List<CatalogBaseVo>> getCatalogs(@RequestParam("catalogId") Long catalogId) {
        List<CatalogBaseVo> catalogs = categoryService.getCatalogBaseVoPath(catalogId);
        return R.ok(catalogs);
    }


    @GetMapping("/list/tree")
    public R<List<CategoryEntity>> listTree() throws BlockException {
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
        categoryService.updateCascade(category);

        return R.ok();
    }

    /**
     * 修改菜单排序
     *
     * @param category
     * @return
     */
    @RequestMapping("/update/sort")
    public R updateSort(@RequestBody List<CategoryEntity> category) {
        // 批量修改
        categoryService.updateBatchById(category);
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
