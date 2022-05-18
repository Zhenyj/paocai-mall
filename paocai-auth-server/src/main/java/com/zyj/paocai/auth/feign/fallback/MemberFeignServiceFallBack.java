package com.zyj.paocai.auth.feign.fallback;

import com.zyj.paocai.auth.feign.MemberFeignService;
import com.zyj.paocai.common.entity.to.UserLoginTo;
import com.zyj.paocai.common.entity.to.UserRegisterTo;
import com.zyj.paocai.common.entity.vo.MemberRespVo;
import com.zyj.paocai.common.exception.BizCodeEnum;
import com.zyj.paocai.common.utils.R;
import com.zyj.paocai.common.utils.RRException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author lulx
 * @date 2022-05-18 13:17
 **/
@Component
@Slf4j
public class MemberFeignServiceFallBack implements MemberFeignService {
    @Override
    public R regist(UserRegisterTo userRegisterTo) {
        throw new RRException("注册失败", BizCodeEnum.MEMBER_SERVICE_EXCEPTION.getCode());
    }

    @Override
    public R<MemberRespVo> login(UserLoginTo userLoginTo) {
        throw new RRException("登录失败", BizCodeEnum.MEMBER_SERVICE_EXCEPTION.getCode());
    }
}
