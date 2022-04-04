package com.zyj.paocai.product.controller;

import com.zyj.paocai.common.utils.PageUtils;
import com.zyj.paocai.common.utils.R;
import com.zyj.paocai.product.entity.BrandEntity;
import com.zyj.paocai.product.service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;



/**
 * 品牌
 *
 * @author lulx
 * @email 
 * @date 2022-03-15 21:19:43
 */
@RestController
@RequestMapping("product/brand")
public class BrandController {
    @Autowired
    private BrandService brandService;

    /**
     * 获取品牌信息
     * @param brandIds
     * @return
     */
    @GetMapping("/infos")
    public R<List<BrandEntity>> info(@RequestParam("brandIds") List<Long> brandIds) {
        List<BrandEntity> brand = brandService.listByIds(brandIds);
        return R.ok(brand);
    }

    /**
     * 列表
     */
    @RequestMapping("/list")
    public R<PageUtils> list(@RequestParam Map<String, Object> params){
        PageUtils page = brandService.queryPage(params);

        return R.ok(page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{brandId}")
    public R<BrandEntity> info(@PathVariable("brandId") Long brandId){
		BrandEntity brand = brandService.getById(brandId);

        return R.ok(brand);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody BrandEntity brand){
		brandService.save(brand);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody BrandEntity brand){
		brandService.updateById(brand);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] brandIds){
		brandService.removeByIds(Arrays.asList(brandIds));

        return R.ok();
    }

}
