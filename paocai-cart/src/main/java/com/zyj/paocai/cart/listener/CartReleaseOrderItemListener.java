package com.zyj.paocai.cart.listener;

import com.rabbitmq.client.Channel;
import com.zyj.paocai.cart.config.MQConfig;
import com.zyj.paocai.cart.service.CartService;
import com.zyj.paocai.common.entity.to.CartReleaseOrderItemTo;
import com.zyj.paocai.common.exception.BizCodeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * 释放购物车中已提交订单的商品项
 *
 * @author lulx
 * @date 2022-05-15 21:09
 **/
@Slf4j
@Component
@RabbitListener(queues = MQConfig.CART_RELEASE_ORDER_ITEM_QUEUE)
public class CartReleaseOrderItemListener {

    @Autowired
    CartService cartService;

    /**
     * 删除购物车中已提交订单的商品
     *
     * @param cartReleaseOrderItemTo
     * @param channel
     * @param message
     * @throws IOException
     */
    @RabbitHandler
    public void handleCartReleaseOrderItem(@Payload CartReleaseOrderItemTo cartReleaseOrderItemTo, Message message, Channel channel) throws IOException {
        log.info("删除相关订单的购物车商品：{}", cartReleaseOrderItemTo.getSkuIdStr());
        MessageProperties messageProperties = message.getMessageProperties();
        try {
            // 删除购物车商品
            cartService.cartReleaseOrderItem(cartReleaseOrderItemTo);
            channel.basicAck(messageProperties.getDeliveryTag(), false);
        } catch (Exception e) {
            log.error(BizCodeEnum.CART_RELEASE_ORDER_ITEM_EXCEPTION.getMsg() + ":", e.getMessage());
            channel.basicReject(messageProperties.getDeliveryTag(), true);
        }
    }
}
