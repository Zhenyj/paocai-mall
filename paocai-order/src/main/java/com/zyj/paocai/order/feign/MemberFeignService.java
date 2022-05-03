package com.zyj.paocai.order.feign;

import com.zyj.paocai.common.entity.to.AddressTo;
import com.zyj.paocai.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

/**
 * @author lulx
 * @date 2022-05-02 13:46
 **/
@FeignClient("paocai-member")
public interface MemberFeignService {

    /**
     * 获取用户收获地址
     * @return
     */
    @GetMapping("/member/memberreceiveaddress/my_address/list")
    public R<List<AddressTo>> getUserReceiveAddress();
}
