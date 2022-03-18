package com.zyj.paocai.product.controller;

import java.util.Arrays;
import java.util.Map;


import com.zyj.paocai.product.entity.SpuInfoEntity;
import com.zyj.paocai.product.service.SpuInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.zyj.paocai.common.utils.PageUtils;
import com.zyj.paocai.common.utils.R;



/**
 * spu信息
 *
 * @author lulx
 * @email 
 * @date 2022-03-15 21:19:43
 */
@RestController
@RequestMapping("product/spuinfo")
public class SpuInfoController {
    @Autowired
    private SpuInfoService spuInfoService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    
    public R<PageUtils> list(@RequestParam Map<String, Object> params){
        PageUtils page = spuInfoService.queryPage(params);

        return R.ok(page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    
    public R info(@PathVariable("id") Long id){
		SpuInfoEntity spuInfo = spuInfoService.getById(id);

        return R.ok(spuInfo);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    
    public R save(@RequestBody SpuInfoEntity spuInfo){
		spuInfoService.save(spuInfo);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    
    public R update(@RequestBody SpuInfoEntity spuInfo){
		spuInfoService.updateById(spuInfo);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    
    public R delete(@RequestBody Long[] ids){
		spuInfoService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
