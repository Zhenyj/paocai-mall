package com.zyj.paocai.order.feign.fallback;

import com.zyj.paocai.common.entity.vo.CartSkuItem;
import com.zyj.paocai.common.exception.BizCodeEnum;
import com.zyj.paocai.common.utils.R;
import com.zyj.paocai.common.utils.RRException;
import com.zyj.paocai.order.feign.ProductFeignService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author lulx
 * @date 2022-05-18 13:29
 **/
@Slf4j
@Component
public class ProductFeignServiceFallBack implements ProductFeignService {
    @Override
    public R<List<CartSkuItem>> getSkuItems(List<Long> skuIds) {
        throw new RRException(BizCodeEnum.PRODUCT_OF_CART_EXCEPTION.getMsg() + ":" + skuIds.toString(), BizCodeEnum.PRODUCT_OF_CART_EXCEPTION.getCode());
    }

    @Override
    public R<CartSkuItem> getCartSkuItem(Long skuId) {
        throw new RRException(BizCodeEnum.PRODUCT_OF_CART_EXCEPTION.getMsg() + ",skuId:" + skuId, BizCodeEnum.PRODUCT_OF_CART_EXCEPTION.getCode());
    }
}
