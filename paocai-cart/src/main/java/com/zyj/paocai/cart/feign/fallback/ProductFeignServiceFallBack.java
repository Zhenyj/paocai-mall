package com.zyj.paocai.cart.feign.fallback;

import com.zyj.paocai.cart.feign.ProductFeignService;
import com.zyj.paocai.common.entity.vo.BrandVo;
import com.zyj.paocai.common.entity.vo.CartSkuItem;
import com.zyj.paocai.common.exception.BizCodeEnum;
import com.zyj.paocai.common.utils.R;
import com.zyj.paocai.common.utils.RRException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author lulx
 * @date 2022-05-18 13:23
 **/
@Component
@Slf4j
public class ProductFeignServiceFallBack implements ProductFeignService {
    @Override
    public R<BrandVo> getBrandInfo(Long brandId) {
        throw new RRException(BizCodeEnum.PRODUCT_BRAND_EXCEPTION.getMsg(),
                BizCodeEnum.PRODUCT_BRAND_EXCEPTION.getCode());
    }

    @Override
    public R<CartSkuItem> getCartSkuItem(Long skuId) {
        throw new RRException(BizCodeEnum.PRODUCT_OF_CART_EXCEPTION.getMsg(),
                BizCodeEnum.PRODUCT_OF_CART_EXCEPTION.getCode());
    }
}
