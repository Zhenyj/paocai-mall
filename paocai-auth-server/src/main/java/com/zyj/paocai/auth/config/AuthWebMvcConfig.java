package com.zyj.paocai.auth.config;

import com.zyj.paocai.auth.interceptor.LoginInfoInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author lulx
 * @date 2022-04-09 22:48
 **/
@Component
public class AuthWebMvcConfig implements WebMvcConfigurer {

    @Autowired
    LoginInfoInterceptor loginInfoInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

    }
}
