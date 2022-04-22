package com.zyj.paocai.cart.feign;

import com.zyj.paocai.common.entity.vo.BrandVo;
import com.zyj.paocai.common.entity.vo.CartSkuItem;
import com.zyj.paocai.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author lulx
 * @date 2022-04-22 0:41
 **/
@FeignClient("paocai-product")
public interface ProductFeignService {

    @RequestMapping("/product/brand/info/{brandId}")
    public R<BrandVo> getBrandInfo(@PathVariable("brandId") Long brandId);

    @GetMapping("/product/skuinfo/skuItem/{skuId}")
    public R<CartSkuItem> getCartSkuItem(@PathVariable("skuId") Long skuId);
}
