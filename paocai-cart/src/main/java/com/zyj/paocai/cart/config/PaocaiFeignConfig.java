package com.zyj.paocai.cart.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * @author lulx
 * @date 2022-05-02 22:56
 **/
@Configuration
public class PaocaiFeignConfig {

    /**
     * 为防止feign进行远程调用时，丢失请求头
     *
     * @return
     */
    @Bean
    public RequestInterceptor requestInterceptor() {
        RequestInterceptor requestInterceptor = new RequestInterceptor() {
            @Override
            public void apply(RequestTemplate template) {
                // 1、RequestContextHolder获取到原始请求
                ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
                if (attributes != null) {
                    HttpServletRequest request = attributes.getRequest();
                    // 2、同步请求头数据,Cookie
                    template.header("Cookie", request.getHeader("Cookie"));
                }
            }
        };
        return requestInterceptor;
    }
}
