package com.zyj.paocai.order.web;

import com.alipay.api.AlipayApiException;
import com.zyj.paocai.common.utils.R;
import com.zyj.paocai.order.config.pay.AlipayTemplate;
import com.zyj.paocai.order.entity.vo.PayVo;
import com.zyj.paocai.order.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author lulx
 * @date 2022-05-19 17:12
 **/
@RestController
public class PayController {

    @Autowired
    private AlipayTemplate alipayTemplate;

    @Autowired
    OrderService orderService;

    /**
     * 支付宝支付
     *
     *
     * @return
     * @throws AlipayApiException
     */
    @PostMapping(value = "/order/payOrder")
    public R<String> payOrderByZfb(@RequestBody PayVo payVo) throws AlipayApiException {
        // 获取到支付宝给我们传来的所有请求数据
        payVo = orderService.getOrderPay(payVo);
        String s = alipayTemplate.pay(payVo);
        R<String> r = R.ok();
        r.setData(s);
        return r;
    }
}
