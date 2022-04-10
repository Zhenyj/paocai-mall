package com.zyj.paocai.product.config;

import com.zyj.paocai.product.interceptor.LoginInfoInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 注册拦截器
 *
 * @author lulx
 * @date 2022-04-09 21:03
 **/
@Component
public class ProductWebConfig implements WebMvcConfigurer {

    @Autowired
    LoginInfoInterceptor loginInfoInterceptor;

    /**
     * 注册拦截器
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 注册用户信息拦截器
        registry.addInterceptor(loginInfoInterceptor).addPathPatterns("/product/index/data");
    }
}
