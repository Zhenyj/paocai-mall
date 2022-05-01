package com.zyj.paocai.cart.config;

import com.zyj.paocai.common.constant.Constant;
import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.session.web.http.CookieSerializer;
import org.springframework.session.web.http.DefaultCookieSerializer;

/**
 * @author lulx
 * @date 2022-04-08 10:41
 **/
@Configuration
public class SessionConfig implements BeanClassLoaderAware {

    private ClassLoader loader;

    /**
     * cookie序列化
     *
     * @return
     */
    @Bean
    public CookieSerializer cookieSerializer() {
        DefaultCookieSerializer serializer = new DefaultCookieSerializer();
        serializer.setDomainName(Constant.PAOCAI_DOMAIN);
        serializer.setCookiePath("/");
        serializer.setCookieName(Constant.PAOCAI_COOKIE_NAME);
        return serializer;
    }

    @Bean
    public RedisSerializer<Object> springSessionDefaultRedisSerializer() {
        return new GenericJackson2JsonRedisSerializer();
    }

    @Override
    public void setBeanClassLoader(ClassLoader classLoader) {
        this.loader = classLoader;
    }
}
