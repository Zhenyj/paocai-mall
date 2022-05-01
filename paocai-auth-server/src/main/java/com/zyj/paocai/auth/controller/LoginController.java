package com.zyj.paocai.auth.controller;

import com.zyj.paocai.auth.feign.MemberFeignService;
import com.zyj.paocai.auth.utils.JwtUtils;
import com.zyj.paocai.auth.vo.UserRegisterVo;
import com.zyj.paocai.common.constant.AuthConstant;
import com.zyj.paocai.common.constant.Constant;
import com.zyj.paocai.common.entity.to.UserLoginTo;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
    public R<MemberRespVo> login(@RequestBody @Valid UserLoginTo userLoginTo, BindingResult result,
                                 HttpSession session, HttpServletResponse response) {
        // 校验
        if (result.hasErrors()) {
            return R.error(BizCodeEnum.VALID_EXCEPTION.getCode(), result.getFieldError().getDefaultMessage());
        }
        R<MemberRespVo> r = memberFeignService.login(userLoginTo);
        if (!Constant.SUCCESS_CODE.equals(r.getCode())) {
            return R.error(r.getCode(), r.getMsg());
        }
        MemberRespVo member = r.getData();
        // 将用户信息放入session中（redis）
        session.setAttribute(AuthConstant.LOGIN_USER, member);
        // 过期时间一天
        session.setMaxInactiveInterval(60 * 60 * 24);
        String token = JwtUtils.getToken(member);
        Cookie tokenCookie = new Cookie("token", token);
        tokenCookie.setMaxAge(60 * 60 * 24 * 7);
        tokenCookie.setDomain(Constant.PAOCAI_DOMAIN);
        tokenCookie.setPath("/");
        response.addCookie(tokenCookie);
        return R.ok(member);
    }

    /**
     * 从token中解析出用户信息返回
     * @param request
     * @return
     */
    @RequestMapping("/user/cookie")
    public R<MemberRespVo> getUserFromJwt(HttpServletRequest request,HttpSession session) {
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("token")) {
                try {
                    // 从token中解析出用户信息返回
                    MemberRespVo member = JwtUtils.validateToken(cookie.getValue());
                    if (member == null) {
                        return R.error(BizCodeEnum.TOKEN_EXCEPTION.getCode(), BizCodeEnum.TOKEN_EXCEPTION.getMsg());
                    }
                    // 将用户信息放入session中（redis）
                    session.setAttribute(AuthConstant.LOGIN_USER, member);
                    return R.ok(member);
                } catch (Exception e) {
                    return R.error(BizCodeEnum.TOKEN_EXCEPTION.getCode(), BizCodeEnum.TOKEN_EXCEPTION.getMsg());
                }
            }
        }
        // 请求未携带token
        return R.error(BizCodeEnum.TOKEN_EXCEPTION.getCode(), BizCodeEnum.TOKEN_EXCEPTION.getMsg());
    }
}
