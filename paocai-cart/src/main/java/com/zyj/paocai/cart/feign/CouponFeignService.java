package com.zyj.paocai.cart.feign;

import com.zyj.paocai.cart.feign.fallback.CouponFeignServiceFallBack;
import com.zyj.paocai.common.entity.to.SkuPromotionTo;
import com.zyj.paocai.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author lulx
 * @date 2022-04-22 17:03
 **/
@FeignClient(value = "paocai-coupon",fallback = CouponFeignServiceFallBack.class)
public interface CouponFeignService {
    @GetMapping("/coupon/skufullreduction/sku_promotion/{skuId}")
    public R<SkuPromotionTo> getSkuPromotion(@PathVariable("skuId") Long skuId);
}
