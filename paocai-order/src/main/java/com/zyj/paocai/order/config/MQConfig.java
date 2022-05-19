package com.zyj.paocai.order.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * 消息队列配置
 *
 * @author lulx
 * @date 2022-05-11 11:05
 **/
@Configuration
public class MQConfig {

    public static final Long DELAY_QUEUE_MESSAGE_TTL = 1000 * 30L;
    public static final String ORDER_RELEASE_ORDER_QUEUE = "order.release.order.queue";
    public static final String ORDER_DELAY_QUEUE = "order.delay.queue";
    public static final String ORDER_RELEASE_ORDER_ROUTING_KEY = "order.release.order";
    public static final String ORDER_CREATE_ORDER_ROUTING_KEY = "order.create.order";
    public static final String ORDER_RELEASE_OTHER_ROUTING_KEY = "order.release.other";
    public static final String ORDER_EVENT_EXCHANGE = "order.event.exchange";
    public static final String STOCK_RELEASE_STOCK_QUEUE = "stock.release.stock.queue";
    /** 释放购物车中的商品 */
    public static final String CART_RELEASE_ORDER_ITEM_QUEUE = "cart.release.order.item.queue";
    public static final String CART_RELEASE_ORDER_ITEM_ROUTING_KEY = "cart.release.order.item";

    /**
     * 死信队列，设置队列的过期时间，通过死信路由到指定队列上
     * 只监听死信队列，用于定时判断订单是否已经支付
     *
     * @return
     */
    @Bean(ORDER_DELAY_QUEUE)
    public Queue orderDelayQueue() {
        Map<String, Object> args = new HashMap<>(3);
        args.put("x-dead-letter-exchange", ORDER_EVENT_EXCHANGE);
        args.put("x-dead-letter-routing-key", ORDER_RELEASE_ORDER_ROUTING_KEY);
        args.put("x-message-ttl", DELAY_QUEUE_MESSAGE_TTL);
        return new Queue(ORDER_DELAY_QUEUE, true, false, false, args);
    }

    @Bean(ORDER_RELEASE_ORDER_QUEUE)
    public Queue orderReleaseOrderQueue() {
        return new Queue(ORDER_RELEASE_ORDER_QUEUE, true, false, false);
    }

    @Bean(ORDER_EVENT_EXCHANGE)
    public Exchange orderEventExchange() {
        TopicExchange exchange = new TopicExchange(ORDER_EVENT_EXCHANGE, true, false);
        return exchange;
    }

    @Bean
    public Binding orderCreateOrderBinding() {
        Binding binding = new Binding(ORDER_DELAY_QUEUE,
                Binding.DestinationType.QUEUE,
                ORDER_EVENT_EXCHANGE,
                ORDER_CREATE_ORDER_ROUTING_KEY,
                null);
        return binding;
    }

    @Bean
    public Binding orderReleaseOrderBinding() {
        Binding binding = new Binding(ORDER_RELEASE_ORDER_QUEUE,
                Binding.DestinationType.QUEUE,
                ORDER_EVENT_EXCHANGE,
                ORDER_RELEASE_ORDER_ROUTING_KEY,
                null);
        return binding;
    }

    /**
     * 订单释放与库存释放进行绑定
     *
     * @return
     */
    @Bean
    public Binding orderReleaseOtherBinding() {
        Binding binding = new Binding(STOCK_RELEASE_STOCK_QUEUE,
                Binding.DestinationType.QUEUE,
                ORDER_EVENT_EXCHANGE,
                ORDER_RELEASE_OTHER_ROUTING_KEY + ".#",
                null);
        return binding;
    }

    /**
     * 释放购物车中的订单商品
     */
    @Bean
    public Binding cartReleaseOrderItem() {
        Binding binding = new Binding(CART_RELEASE_ORDER_ITEM_QUEUE,
                Binding.DestinationType.QUEUE,
                ORDER_EVENT_EXCHANGE,
                CART_RELEASE_ORDER_ITEM_ROUTING_KEY,
                null);
        return binding;
    }
}
