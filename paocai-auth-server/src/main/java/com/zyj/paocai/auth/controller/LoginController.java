package com.zyj.paocai.auth.controller;

import com.zyj.paocai.auth.feign.MemberFeignService;
import com.zyj.paocai.common.entity.to.UserLoginTo;
import com.zyj.paocai.auth.vo.UserRegisterVo;
import com.zyj.paocai.common.constant.Constant;
import com.zyj.paocai.common.entity.to.UserRegisterTo;
import com.zyj.paocai.common.entity.vo.MemberRespVo;
import com.zyj.paocai.common.exception.BizCodeEnum;
import com.zyj.paocai.common.utils.R;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

/**
 * @author lulx
 * @date 2022-04-08 10:36
 **/
@RestController
public class LoginController {
    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    MemberFeignService memberFeignService;

    /**
     * 用户注册
     *
     * @param userRegisterVo
     * @return
     */
    @PostMapping("/regist")
    public R regist(@RequestBody @Valid UserRegisterVo userRegisterVo, BindingResult result) {
        // 校验
        if (result.hasErrors()) {
            return R.error(BizCodeEnum.VALID_EXCEPTION.getCode(), result.getFieldError().getDefaultMessage());
        }
        // TODO 可以增加短信验证码认证校验等
        // 调用会员服务进行注册
        UserRegisterTo userRegisterTo = new UserRegisterTo();
        BeanUtils.copyProperties(userRegisterVo, userRegisterTo);
        R r = memberFeignService.regist(userRegisterTo);
        if (!Constant.SUCCESS_CODE.equals(r.getCode())) {
            return R.error(r.getCode(), r.getMsg());
        }
        return R.ok();
    }

    /**
     * 账号密码登录
     *
     * @param userLoginTo
     * @param result
     * @param session
     * @return
     */
    @PostMapping("/login")
    public R<MemberRespVo> login(@RequestBody @Valid UserLoginTo userLoginTo, BindingResult result, HttpSession session) {
        // 校验
        if (result.hasErrors()) {
            return R.error(BizCodeEnum.VALID_EXCEPTION.getCode(), result.getFieldError().getDefaultMessage());
        }
        R<MemberRespVo> r = memberFeignService.login(userLoginTo);
        if (!Constant.SUCCESS_CODE.equals(r.getCode())) {
            return R.error(r.getCode(), r.getMsg());
        }
        return R.ok(r.getData());
    }
}
