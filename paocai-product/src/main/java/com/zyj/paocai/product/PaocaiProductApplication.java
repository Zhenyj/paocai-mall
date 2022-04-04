package com.zyj.paocai.product;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableCaching
@EnableFeignClients(basePackages = {"com.zyj.paocai.product.feign" })
@EnableTransactionManagement
@EnableDiscoveryClient
@SpringBootApplication
public class PaocaiProductApplication {

    public static void main(String[] args) {
        SpringApplication.run(PaocaiProductApplication.class, args);
    }

}
