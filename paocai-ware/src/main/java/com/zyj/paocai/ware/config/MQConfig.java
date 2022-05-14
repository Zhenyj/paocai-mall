package com.zyj.paocai.ware.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * @author lulx
 * @date 2022-05-11 11:31
 **/
@Configuration
public class MQConfig {

    public static final String STOCK_EVENT_EXCHANGE = "stock.event.exchange";
    public static final String STOCK_RELEASE_STOCK_QUEUE = "stock.release.stock.queue";
    public static final String STOCK_DELAY_QUEUE = "stock.delay.queue";
    public static final String STOCK_RELEASE_ROUTING_KEY = "stock.release";
    public static final String STOCK_LOCKED_ROUTING_KEY = "stock.locked";
    public static final Long STOCK_DELAY_QUEUE_TTL = 1000 * 10L;

    @Bean(STOCK_EVENT_EXCHANGE)
    public Exchange stockEventExchange() {
        TopicExchange exchange = new TopicExchange(STOCK_EVENT_EXCHANGE, true, false);
        return exchange;
    }

    @Bean(STOCK_RELEASE_STOCK_QUEUE)
    public Queue stockReleaseStockQueue() {
        Queue queue = new Queue(STOCK_RELEASE_STOCK_QUEUE, true, false, false);
        return queue;
    }

    @Bean(STOCK_DELAY_QUEUE)
    public Queue stockDelayQueue() {
        Map<String, Object> args = new HashMap<>(3);
        args.put("x-dead-letter-exchange", STOCK_EVENT_EXCHANGE);
        args.put("x-dead-letter-routing-key", STOCK_RELEASE_ROUTING_KEY);
        args.put("x-message-ttl", STOCK_DELAY_QUEUE_TTL);
        Queue queue = new Queue(STOCK_DELAY_QUEUE, true, false, false, args);
        return queue;
    }

    @Bean
    public Binding stockReleaseBinding() {
        Binding binding = new Binding(STOCK_RELEASE_STOCK_QUEUE,
                Binding.DestinationType.QUEUE,
                STOCK_EVENT_EXCHANGE,
                STOCK_RELEASE_ROUTING_KEY + ".#",
                null);
        return binding;
    }

    @Bean
    public Binding stockLockedBinding() {
        Binding binding = new Binding(STOCK_DELAY_QUEUE,
                Binding.DestinationType.QUEUE,
                STOCK_EVENT_EXCHANGE,
                STOCK_LOCKED_ROUTING_KEY,
                null);
        return binding;
    }

}

