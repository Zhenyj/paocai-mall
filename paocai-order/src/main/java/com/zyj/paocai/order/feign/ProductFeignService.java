package com.zyj.paocai.order.feign;

import com.zyj.paocai.common.entity.vo.CartSkuItem;
import com.zyj.paocai.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author lulx
 * @date 2022-04-29 17:06
 **/
@FeignClient("paocai-product")
public interface ProductFeignService {

    @PostMapping("/product/skuinfo/skuItems")
    public R<List<CartSkuItem>> getSkuItems(@RequestParam("skuIds") List<Long> skuIds);
}
