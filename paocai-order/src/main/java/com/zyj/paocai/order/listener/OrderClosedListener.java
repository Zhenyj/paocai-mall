package com.zyj.paocai.order.listener;

import com.rabbitmq.client.Channel;
import com.zyj.paocai.common.exception.BizCodeEnum;
import com.zyj.paocai.order.config.MQConfig;
import com.zyj.paocai.order.entity.OrderEntity;
import com.zyj.paocai.order.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @author lulx
 * @date 2022-05-11 16:18
 **/
@Slf4j
@Component
@RabbitListener(queues = MQConfig.ORDER_RELEASE_ORDER_QUEUE)
public class OrderClosedListener {

    @Autowired
    private OrderService orderService;

    @RabbitHandler
    public void listener(OrderEntity order, Channel channel, Message message) throws IOException {
        log.info("收到订单过期信息:{}，如未支付将自动关闭", order.getOrderSn());
        MessageProperties messageProperties = message.getMessageProperties();
        try {
            orderService.closeOrder(order);
            // 手动调用支付宝收单或者设置自动关单
            channel.basicAck(messageProperties.getDeliveryTag(), false);
        } catch (Exception e) {
            log.error(BizCodeEnum.ORDER_CLOSE_EXCEPTION.getMsg()+":", e.getMessage());
            channel.basicReject(message.getMessageProperties().getDeliveryTag(), true);
        }
    }
}
