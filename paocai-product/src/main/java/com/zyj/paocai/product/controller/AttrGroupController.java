package com.zyj.paocai.product.controller;

import com.zyj.paocai.common.utils.PageUtils;
import com.zyj.paocai.common.utils.R;
import com.zyj.paocai.product.entity.AttrEntity;
import com.zyj.paocai.product.entity.AttrGroupEntity;
import com.zyj.paocai.product.entity.vo.AttrGroupWithAttrsVo;
import com.zyj.paocai.product.entity.vo.AttrGroupWithCatelogPathVo;
import com.zyj.paocai.product.service.AttrGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;


/**
 * 属性分组
 *
 * @author lulx
 * @email
 * @date 2022-03-15 21:19:43
 */
@RestController
@RequestMapping("product/attrgroup")
public class AttrGroupController {
    @Autowired
    private AttrGroupService attrGroupService;

    /**
     * 获取分类下所有分组&关联属性
     * @param catelogId
     * @return
     */
    @GetMapping("/{catelogId}/withattr")
    public R<List<AttrGroupWithAttrsVo>> getAttrGroupWithAttrs(@PathVariable("catelogId") Long catelogId){
        List<AttrGroupWithAttrsVo> vos = attrGroupService.getAttrGroupWithAttrs(catelogId);
        return R.ok(vos);
    }

    /**
     * 获取属性分组的关联的所有属性
     *
     * @param attrGroupId
     * @return
     */
    @GetMapping("/{attrGroupId}/attr/relation")
    public R<List<AttrEntity>> getAttrsByAttrGroupId(@PathVariable("attrGroupId") Long attrGroupId) {
        List<AttrEntity> attrs = attrGroupService.getAttrsByAttrGroupId(attrGroupId);
        return R.ok(attrs);
    }

    /**
     * 获取分类属性分组
     *
     * @param catelogId
     * @param params
     * @return
     */
    @GetMapping("/list/{catelogId}")
    public R<PageUtils> listByCatelogId(@PathVariable("catelogId") Long catelogId,
                                        @RequestParam Map<String, Object> params) {
        PageUtils page = attrGroupService.listByCatelogId(catelogId, params);
        return R.ok(page);
    }

    /**
     * 列表
     */
    @RequestMapping("/list")
    public R<PageUtils> list(@RequestParam Map<String, Object> params) {
        PageUtils page = attrGroupService.queryPage(params);

        return R.ok(page);
    }


    /**
     * 信息
     */
    @GetMapping("/info/{attrGroupId}")
    public R info(@PathVariable("attrGroupId") Long attrGroupId) {
        AttrGroupWithCatelogPathVo vo = attrGroupService.getAttrGroupWithCatelogPathVoById(attrGroupId);
        return R.ok(vo);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody AttrGroupEntity attrGroup) {
        attrGroupService.save(attrGroup);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody AttrGroupEntity attrGroup) {
        attrGroupService.updateById(attrGroup);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] attrGroupIds) {
        attrGroupService.removeByIds(Arrays.asList(attrGroupIds));

        return R.ok();
    }

}
