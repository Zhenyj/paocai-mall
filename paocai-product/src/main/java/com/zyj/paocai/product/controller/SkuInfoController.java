package com.zyj.paocai.product.controller;

import com.zyj.paocai.product.entity.SkuInfoEntity;
import com.zyj.paocai.product.service.SkuInfoService;
import com.zyj.paocai.common.utils.PageUtils;
import com.zyj.paocai.common.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;



/**
 * sku信息
 *
 * @author lulx
 * @email 
 * @date 2022-03-15 21:19:43
 */
@RestController
@RequestMapping("product/skuinfo")
public class SkuInfoController {
    @Autowired
    private SkuInfoService skuInfoService;

    @PostMapping("/skunameinfos")
    R<Map<Long,String>> getSkuNameInfos(@RequestBody List<Long> skuIds){
        Map<Long,String> map = skuInfoService.getSkuNameInfos(skuIds);
        return R.ok(map);
    }

    /**
     * 列表
     */
    @RequestMapping("/list")
    public R<PageUtils> list(@RequestParam Map<String, Object> params){
        PageUtils page = skuInfoService.queryPageByCondition(params);

        return R.ok(page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{skuId}")
    
    public R info(@PathVariable("skuId") Long skuId){
		SkuInfoEntity skuInfo = skuInfoService.getById(skuId);

        return R.ok(skuInfo);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    
    public R save(@RequestBody SkuInfoEntity skuInfo){
		skuInfoService.save(skuInfo);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    
    public R update(@RequestBody SkuInfoEntity skuInfo){
		skuInfoService.updateById(skuInfo);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    
    public R delete(@RequestBody Long[] skuIds){
		skuInfoService.removeByIds(Arrays.asList(skuIds));

        return R.ok();
    }

}
