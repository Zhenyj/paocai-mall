package com.zyj.paocai.order.feign;

import com.zyj.paocai.common.entity.vo.CartSkuItem;
import com.zyj.paocai.common.utils.R;
import com.zyj.paocai.order.feign.fallback.ProductFeignServiceFallBack;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author lulx
 * @date 2022-04-29 17:06
 **/
@FeignClient(value = "paocai-product", fallback = ProductFeignServiceFallBack.class)
public interface ProductFeignService {

    @PostMapping("/product/skuinfo/skuItems")
    public R<List<CartSkuItem>> getSkuItems(@RequestParam("skuIds") List<Long> skuIds);

    @GetMapping("/product/skuinfo/skuItem/{skuId}")
    public R<CartSkuItem> getCartSkuItem(@PathVariable("skuId") Long skuId);
}
