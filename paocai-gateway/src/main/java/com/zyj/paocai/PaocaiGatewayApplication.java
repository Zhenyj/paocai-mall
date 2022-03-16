package com.zyj.paocai;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class PaocaiGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(PaocaiGatewayApplication.class, args);
    }

}
