package com.zyj.paocai.cart.interceptor;

import com.zyj.paocai.common.constant.AuthConstant;
import com.zyj.paocai.common.entity.vo.MemberRespVo;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author lulx
 * @date 2022-04-21 23:54
 **/
@Component
public class CartInterceptor implements HandlerInterceptor {

    public static ThreadLocal<MemberRespVo> threadLocal = new ThreadLocal();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession();
        MemberRespVo member = (MemberRespVo) session.getAttribute(AuthConstant.LOGIN_USER);
        if (member == null) {
            throw new RuntimeException("对不起，请先登录后再进行操作！");
        }
        threadLocal.set(member);
        return HandlerInterceptor.super.preHandle(request, response, handler);
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        // 释放
        threadLocal.remove();
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }
}
