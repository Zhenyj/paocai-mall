package com.zyj.paocai.order.config;

import com.zyj.paocai.order.interceptor.LoginInfoInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author lulx
 * @date 2022-04-22 13:28
 **/
@Component
public class OrderWebConfig implements WebMvcConfigurer {

    @Autowired
    LoginInfoInterceptor loginInfoInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginInfoInterceptor).addPathPatterns("/**");
        WebMvcConfigurer.super.addInterceptors(registry);
    }
}
