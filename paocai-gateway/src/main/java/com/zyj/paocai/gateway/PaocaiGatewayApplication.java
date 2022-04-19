package com.zyj.paocai.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@EnableDiscoveryClient
@SpringBootApplication(scanBasePackages = {"com.zyj.paocai"}, exclude = {DataSourceAutoConfiguration.class})
public class PaocaiGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(PaocaiGatewayApplication.class, args);
    }

}
