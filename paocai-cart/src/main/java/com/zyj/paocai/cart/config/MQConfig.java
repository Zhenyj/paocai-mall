package com.zyj.paocai.cart.config;

import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author lulx
 * @date 2022-05-15 21:08
 **/
@Configuration
public class MQConfig {

    /** 释放购物车中的商品 */
    public static final String CART_RELEASE_ORDER_ITEM_QUEUE = "cart.release.order.item.queue";

    public static final String CART_EVENT_EXCHANGE = "cart.event.exchange";

    @Bean(CART_RELEASE_ORDER_ITEM_QUEUE)
    public Queue cartReleaseQueue() {
        return new Queue(CART_RELEASE_ORDER_ITEM_QUEUE, true, false, false);
    }

    @Bean(CART_EVENT_EXCHANGE)
    public Exchange cartEventExchange() {
        return new TopicExchange(CART_EVENT_EXCHANGE, true, false);
    }
}
