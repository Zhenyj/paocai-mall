package com.zyj.paocai.ware.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

/**
 * @author lulx
 * @date 2022-05-25 15:38
 **/
@Configuration
public class RedissonConfig {
    /**
     * 所有对Redisson的使用都是通过RedissonClient对象
     *
     * @return
     * @throws IOException
     */
    @Bean(destroyMethod = "shutdown")
    RedissonClient redissonClient() throws IOException {
        // 创建配置useClusterServers
        Config config = new Config();
//        使用集群服务器
//        config.useClusterServers()
//                .addNodeAddress("127.0.0.1:7001", "127.0.0.1:7002");
        // 单节点模式useSingleServer
        config.useSingleServer().setAddress("redis://192.168.136.12:6379");
        // 创建redisson实例
        RedissonClient redissonClient = Redisson.create(config);
        return redissonClient;
    }
}
