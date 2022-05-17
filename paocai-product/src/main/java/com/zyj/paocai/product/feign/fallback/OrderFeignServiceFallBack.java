package com.zyj.paocai.product.feign.fallback;

import com.zyj.paocai.common.entity.vo.OrderStatusNumsVo;
import com.zyj.paocai.common.utils.R;
import com.zyj.paocai.product.feign.OrderFeignService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 服务降级
 *
 * @author lulx
 * @date 2022-05-17 10:17
 **/
@Slf4j
@Component
public class OrderFeignServiceFallBack implements OrderFeignService {

    @Override
    public R<OrderStatusNumsVo> getOrderStatusNumsInfo(Long memberId) {
        log.warn("获取用户订单数量服务降级");
        OrderStatusNumsVo vo = new OrderStatusNumsVo();
        vo.setCartNum(0);
        vo.setWaitCommentNum(0);
        vo.setWaitPayNum(0);
        vo.setWaitDeliverNum(0);
        vo.setWaitReceiveNum(0);
        return R.ok(vo);
    }
}
