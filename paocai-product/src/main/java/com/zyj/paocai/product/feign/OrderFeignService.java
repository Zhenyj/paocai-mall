package com.zyj.paocai.product.feign;

import com.zyj.paocai.common.entity.vo.OrderStatusNumsVo;
import com.zyj.paocai.common.utils.R;
import com.zyj.paocai.product.feign.fallback.OrderFeignServiceFallBack;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author lulx
 * @date 2022-04-07 17:22
 **/
@FeignClient(value = "paocai-order",fallback = OrderFeignServiceFallBack.class)
public interface OrderFeignService {

    @PostMapping("/order/order/order_status_num")
    R<OrderStatusNumsVo> getOrderStatusNumsInfo(@RequestParam("memberId") Long memberId);
}
