package com.zyj.paocai.cart.feign.fallback;

import com.zyj.paocai.cart.feign.CouponFeignService;
import com.zyj.paocai.common.entity.to.SkuPromotionTo;
import com.zyj.paocai.common.exception.BizCodeEnum;
import com.zyj.paocai.common.utils.R;
import com.zyj.paocai.common.utils.RRException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author lulx
 * @date 2022-05-18 13:20
 **/
@Component
@Slf4j
public class CouponFeignServiceFallBack implements CouponFeignService {
    @Override
    public R<SkuPromotionTo> getSkuPromotion(Long skuId) {
        throw new RRException(BizCodeEnum.COUPON_OF_PRODUCT_EXCEPTION.getMsg(),
                BizCodeEnum.COUPON_OF_PRODUCT_EXCEPTION.getCode());
    }
}
