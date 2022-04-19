package com.zyj.paocai.search.feign;

import com.zyj.paocai.common.entity.vo.CatalogBaseVo;
import com.zyj.paocai.common.utils.R;
import com.zyj.paocai.search.vo.AttrRespVo;
import com.zyj.paocai.search.vo.BrandVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author lulx
 * @date 2022-03-15 23:55
 **/
@FeignClient("paocai-product")
public interface ProductFeignService {
    /**
     * 获取属性信息
     * @param attrId
     * @return
     */
    @RequestMapping("/product/attr/info/{attrId}")
    R<AttrRespVo> getAttrInfo(@PathVariable("attrId") Long attrId);

    /**
     * 获取品牌信息
     * @param brandIds
     * @return
     */
    @GetMapping("/product/brand/infos")
    R<List<BrandVo>> getBrandInfo(@RequestParam("brandIds") List<Long> brandIds);

    /**
     * 获取分类路径
     * @param catalogId
     * @return
     */
    @RequestMapping("/product/category/catalogs")
    R<List<CatalogBaseVo>> getCatalogs(@RequestParam("catalogId") Long catalogId);

    @RequestMapping("/product/category/same_level_category")
    R<List<CatalogBaseVo>> getSameLevelCategory(@RequestParam("catalogId") Long catalogId);
}
