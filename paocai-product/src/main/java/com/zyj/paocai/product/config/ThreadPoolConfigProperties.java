package com.zyj.paocai.product.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author lulx
 * @date 2022-04-01 21:58
 **/
@Component
@ConfigurationProperties(prefix = "paocai.thread")
@Data
public class ThreadPoolConfigProperties {
    /**核心线程数*/
    private int corePoolSize;
    /**最大线程数*/
    private int maximumPoolSize;
    /***/
    private long keepAliveTime;

}
