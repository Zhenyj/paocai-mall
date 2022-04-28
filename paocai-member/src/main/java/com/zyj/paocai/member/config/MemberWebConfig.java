package com.zyj.paocai.member.config;

import com.zyj.paocai.member.interceptor.LoginInfoInterceptor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author lulx
 * @date 2022-04-28 15:02
 **/
@Component
public class MemberWebConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginInfoInterceptor())
                .addPathPatterns("/member/memberreceiveaddress/**");
        WebMvcConfigurer.super.addInterceptors(registry);
    }
}
