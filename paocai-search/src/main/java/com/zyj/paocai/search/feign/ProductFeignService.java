package com.zyj.paocai.search.feign;

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
    @RequestMapping("/product/attr/info/{attrId}")
    R<AttrRespVo> getAttrInfo(@PathVariable("attrId") Long attrId);

    @GetMapping("/product/brand/infos")
    R<List<BrandVo>> getBrandInfo(@RequestParam("brandIds") List<Long> brandIds);
}
