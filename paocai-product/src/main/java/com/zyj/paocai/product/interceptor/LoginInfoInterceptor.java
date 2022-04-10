package com.zyj.paocai.product.interceptor;

import com.zyj.paocai.common.constant.AuthConstant;
import com.zyj.paocai.common.entity.vo.MemberRespVo;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * 用户信息拦截器
 *
 * @author lulx
 * @date 2022-04-09 20:40
 **/
@Component
public class LoginInfoInterceptor implements HandlerInterceptor {

    public static ThreadLocal<MemberRespVo> loginInfo = new ThreadLocal<>();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession();
        MemberRespVo member = (MemberRespVo)session.getAttribute(AuthConstant.LOGIN_USER);
        if(member != null){
            loginInfo.set(member);
            return true;
        }
        return HandlerInterceptor.super.preHandle(request, response, handler);
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }
}
