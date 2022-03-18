package com.zyj.paocai.gateway.conifg;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

/**
 * 跨域配置
 *
 * @author lulx
 * @date 2022-03-18 16:10
 **/
@Configuration
public class PaocaiCorsConfiguration {

    @Bean
    public CorsWebFilter corsWebFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

        CorsConfiguration corsConfiguration = new CorsConfiguration();
        // 1、配置跨域
        // 允许哪些头进行跨域
        corsConfiguration.addAllowedHeader("*");
        // 运行哪些请求方式跨域
        corsConfiguration.addAllowedMethod("*");
        // 任意请求来源进行跨域
        corsConfiguration.addAllowedOriginPattern("*");
        // 是否允许携带cookie进行跨域
        corsConfiguration.setAllowCredentials(true);
            //任意路径都进行跨域
        source.registerCorsConfiguration("/**", corsConfiguration);
        
        CorsWebFilter corsWebFilter = new CorsWebFilter( source);
        return corsWebFilter;
    }
}
