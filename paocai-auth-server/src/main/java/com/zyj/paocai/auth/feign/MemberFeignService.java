package com.zyj.paocai.auth.feign;

import com.zyj.paocai.auth.feign.fallback.MemberFeignServiceFallBack;
import com.zyj.paocai.common.entity.to.UserLoginTo;
import com.zyj.paocai.common.entity.to.UserRegisterTo;
import com.zyj.paocai.common.entity.vo.MemberRespVo;
import com.zyj.paocai.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author lulx
 * @date 2022-04-08 10:50
 **/
@FeignClient(value = "paocai-member",fallback = MemberFeignServiceFallBack.class)
public interface MemberFeignService {

    @PostMapping("/member/member/regist")
    R regist(@RequestBody UserRegisterTo userRegisterTo);

    @PostMapping("/member/member/login")
    R<MemberRespVo> login(@RequestBody UserLoginTo userLoginTo);
}
