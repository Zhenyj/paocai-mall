package com.zyj.paocai.product.feign;

import com.zyj.paocai.common.entity.to.SkuPromotionTo;
import com.zyj.paocai.common.entity.to.SkuReductionTo;
import com.zyj.paocai.common.entity.to.SpuBoundsTo;
import com.zyj.paocai.common.entity.vo.SkuBoundsVo;
import com.zyj.paocai.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
 * 优惠券服务远程接口
 *
 * @author lulx
 * @date 2022-04-01 20:26
 **/
@FeignClient("paocai-coupon")
public interface CouponFeignService {
    /**
     * 保存spu积分信息
     *
     * @param spuBoundsTo
     * @return
     */
    @PostMapping("/coupon/spubounds/save")
    R saveSpuBounds(@RequestBody SpuBoundsTo spuBoundsTo);


    /**
     * 保存sku满减信息
     * @param skuReductionTo
     * @return
     */
    @PostMapping("/coupon/skufullreduction/saveinfo")
    R saveSkuReduction(@RequestBody SkuReductionTo skuReductionTo);

    /**
     * 获取商品积分信息
     * @param skuId
     * @return
     */
    @RequestMapping("/coupon/skubounds/bounds/info")
    R<SkuBoundsVo> getBoundsBySkuId(@RequestParam("skuId") Long skuId);

    /**
     * 获取sku优惠信息
     * @param skuId
     * @return
     */
    @GetMapping("/coupon/skufullreduction/sku_promotion/{skuId}")
    R<SkuPromotionTo> getSkuPromotion(@PathVariable("skuId") Long skuId);
}
