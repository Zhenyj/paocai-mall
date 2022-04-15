package com.zyj.paocai.coupon.controller;

import com.zyj.paocai.coupon.entity.SkuBoundsEntity;
import com.zyj.paocai.coupon.service.SkuBoundsService;
import com.zyj.paocai.common.utils.PageUtils;
import com.zyj.paocai.common.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Map;



/**
 * 商品sku积分设置
 *
 * @author lulx
 * @email 
 * @date 2022-03-15 21:22:50
 */
@RestController
@RequestMapping("coupon/skubounds")
public class SkuBoundsController {
    @Autowired
    private SkuBoundsService skuBoundsService;

    /**
     * 根据skuId获取sku积分信息
     * @param skuId
     * @return
     */
    @RequestMapping("/bounds/info")
    public R<SkuBoundsEntity> getBySkuId(@RequestParam("skuId") Long skuId){
        SkuBoundsEntity skuBounds = skuBoundsService.getBySkuId(skuId);
        return R.ok(skuBounds);
    }
    /**
     * 列表
     */
    @RequestMapping("/list")
    public R<PageUtils> list(@RequestParam Map<String, Object> params){
        PageUtils page = skuBoundsService.queryPage(params);

        return R.ok(page);
    }

    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    public R<SkuBoundsEntity> info(@PathVariable("id") Long id){
		SkuBoundsEntity skuBounds = skuBoundsService.getById(id);

        return R.ok(skuBounds);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody SkuBoundsEntity skuBounds){
		skuBoundsService.save(skuBounds);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody SkuBoundsEntity skuBounds){
		skuBoundsService.updateById(skuBounds);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids){
		skuBoundsService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
