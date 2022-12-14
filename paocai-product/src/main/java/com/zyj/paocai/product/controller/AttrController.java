package com.zyj.paocai.product.controller;

import com.zyj.paocai.common.constant.ProductConstant;
import com.zyj.paocai.common.utils.PageUtils;
import com.zyj.paocai.common.utils.R;
import com.zyj.paocai.product.entity.ProductAttrValueEntity;
import com.zyj.paocai.product.entity.vo.AttrInfoVo;
import com.zyj.paocai.product.service.AttrService;
import com.zyj.paocai.product.service.ProductAttrValueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;


/**
 * 商品属性
 *
 * @author lulx
 * @email
 * @date 2022-03-15 21:19:43
 */
@RestController
@RequestMapping("product/attr")
public class AttrController {
    @Autowired
    private AttrService attrService;

    @Autowired
    ProductAttrValueService productAttrValueService;

    /**
     * 获取分类销售属性
     *
     * @param catelogId
     * @param params
     * @return
     */
    @GetMapping("/sale/list/{catelogId}")
    public R<PageUtils> getSaleAttrByCatelogId(@PathVariable("catelogId") Long catelogId, @RequestParam Map<String, Object> params) {
        PageUtils page = attrService.getAttrByCatelogId(catelogId, params, ProductConstant.SALE_ATTR_TYPE);

        return R.ok(page);
    }

    @PostMapping("/update/{spuId}")
    public R updateBaseAttrForSpu(@RequestBody List<ProductAttrValueEntity> attrValueEntities, @PathVariable("spuId") Long spuId) {
        productAttrValueService.updateBaseAttrForSpu(attrValueEntities,spuId);
        return R.ok();
    }

    /**
     * 获取spu规格
     *
     * @param spuId
     * @return
     */
    @GetMapping("/base/listforspu/{spuId}")
    public R<List<ProductAttrValueEntity>> listBaseAttrForSpu(@PathVariable("spuId") Long spuId) {
        List<ProductAttrValueEntity> list = productAttrValueService.listBaseAttrForSpu(spuId);
        return R.ok(list);
    }

    /**
     * 获取分类规格参数
     *
     * @param catelogId
     * @param params
     * @return
     */
    @GetMapping("/base/list/{catelogId}")
    public R<PageUtils> getBaseAttrByCatelogId(@PathVariable("catelogId") Long catelogId, @RequestParam Map<String, Object> params) {
        PageUtils page = attrService.getAttrByCatelogId(catelogId, params, ProductConstant.BASE_ATTR_TYPE);

        return R.ok(page);
    }

    /**
     * 列表
     */
    @RequestMapping("/list")
    public R<PageUtils> list(@RequestParam Map<String, Object> params) {
        PageUtils page = attrService.queryPage(params);

        return R.ok(page);
    }


    /**
     * 查询属性详情
     */
    @RequestMapping("/info/{attrId}")
    public R<AttrInfoVo> info(@PathVariable("attrId") Long attrId) {
        AttrInfoVo attr = attrService.getAttrInfo(attrId);

        return R.ok(attr);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody AttrInfoVo attr) {
        attrService.saveAttr(attr);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody AttrInfoVo attr) {
        attrService.updateAttr(attr);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] attrIds) {
        attrService.removeByIds(Arrays.asList(attrIds));

        return R.ok();
    }

}
