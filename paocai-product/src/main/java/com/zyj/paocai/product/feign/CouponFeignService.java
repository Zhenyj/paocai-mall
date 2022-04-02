package com.zyj.paocai.product.feign;

import com.zyj.paocai.common.entity.to.SkuReductionTo;
import com.zyj.paocai.common.entity.to.SpuBoundsTo;
import com.zyj.paocai.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 优惠券服务远程接口
 * @author lulx
 * @date 2022-04-01 20:26
 **/
@FeignClient("paocai-coupon")
public interface CouponFeignService {
    /**
     * 保存spu积分信息
     * @param spuBoundsTo
     * @return
     */
    @PostMapping("/coupon/spubounds/save")
    R saveSpuBounds (@RequestBody SpuBoundsTo spuBoundsTo);


    @PostMapping("/coupon/skufullreduction/saveinfo")
    R saveSkuReduction (@RequestBody SkuReductionTo skuReductionTo);
}
