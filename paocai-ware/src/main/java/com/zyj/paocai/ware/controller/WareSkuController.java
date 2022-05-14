package com.zyj.paocai.ware.controller;

import com.zyj.paocai.common.entity.to.WareLockTo;
import com.zyj.paocai.common.entity.vo.SkuHasStockVo;
import com.zyj.paocai.common.entity.vo.SkuIdCountVo;
import com.zyj.paocai.common.utils.PageUtils;
import com.zyj.paocai.common.utils.R;
import com.zyj.paocai.ware.entity.WareSkuEntity;
import com.zyj.paocai.ware.service.WareSkuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;


/**
 * 商品库存
 *
 * @author lulx
 * @email
 * @date 2022-03-15 21:24:53
 */
@RestController
@RequestMapping("ware/waresku")
public class WareSkuController {
    @Autowired
    private WareSkuService wareSkuService;

    /**
     * 订单锁定库存
     * @param wareLockTos
     * @return
     */
    @PostMapping("/lock/order")
    public R orderLockStock(@RequestBody List<WareLockTo> wareLockTos) {
        wareSkuService.orderLockStock(wareLockTos);
        return R.ok();
    }


    @PostMapping("/hasstock/{skuId}")
    public R<SkuHasStockVo> getSkuHasStock(@PathVariable("skuId") Long skuId) {
        SkuHasStockVo vo = wareSkuService.getSkuHasStock(skuId);
        return R.ok(vo);
    }

    /**
     * 获取sku是否有库存，包含需要的商品数量
     *
     * @param skuIdCountVos
     * @return
     */
    @PostMapping("/hasstock/batch_nums")
    public R<List<SkuHasStockVo>> getSkuHasStockBatchWithNums(@RequestBody List<SkuIdCountVo> skuIdCountVos) {
        List<SkuHasStockVo> vos = wareSkuService.getSkuHasStockBatchWithNums(skuIdCountVos);
        return R.ok(vos);
    }

    /**
     * 查询sku是否有库存
     */
    @PostMapping("/hasstock/batch")
    public R<List<SkuHasStockVo>> getSkuHasStockBatch(@RequestBody List<Long> skuIds) {
        List<SkuHasStockVo> vos = wareSkuService.getSkuHasStockBatch(skuIds);
        return R.ok(vos);
    }

    /**
     * 列表
     */
    @RequestMapping("/list")
    public R<PageUtils> list(@RequestParam Map<String, Object> params) {
        PageUtils page = wareSkuService.queryPage(params);

        return R.ok(page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id) {
        WareSkuEntity wareSku = wareSkuService.getById(id);

        return R.ok(wareSku);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody WareSkuEntity wareSku) {
        wareSkuService.save(wareSku);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody WareSkuEntity wareSku) {
        wareSkuService.updateById(wareSku);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids) {
        wareSkuService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
