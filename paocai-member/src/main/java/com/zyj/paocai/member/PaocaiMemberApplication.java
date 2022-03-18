package com.zyj.paocai.member;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class PaocaiMemberApplication {

    public static void main(String[] args) {
        SpringApplication.run(PaocaiMemberApplication.class, args);
    }

}
