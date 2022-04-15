package com.zyj.paocai.search.config;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author lulx
 * @date 2022-04-01 1:45
 **/

@Configuration
public class ElasticSearchConfig {

    @Value("${elasticsearch.hostname}")
    private String hostname;

    @Value("${elasticsearch.port}")
    private int port;

    public static final RequestOptions COMMON_OPTIONS;

    static{
        RequestOptions.Builder  builder = RequestOptions.DEFAULT.toBuilder();
        COMMON_OPTIONS = builder.build();
    }

    @Bean
    public RestHighLevelClient esRestClient(){
        RestClientBuilder builder = null;

        builder = RestClient.builder(new HttpHost(hostname, port, "http"));

        RestHighLevelClient client  = new RestHighLevelClient(builder);
        return client;
    }
}
