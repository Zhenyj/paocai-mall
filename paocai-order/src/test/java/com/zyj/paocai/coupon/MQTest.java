package com.zyj.paocai.coupon;

import com.rabbitmq.client.Channel;
import com.zyj.paocai.order.PaocaiOrderApplication;
import com.zyj.paocai.order.config.MQConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.UnsupportedEncodingException;

/**
 * rabbitmq测试
 *
 * @author lulx
 * @date 2022-05-11 15:15
 **/
@SpringBootTest(classes = {PaocaiOrderApplication.class})
@DisplayName("rabbitmq测试")
public class MQTest {

    @Autowired
    RabbitTemplate rabbitTemplate;

    @Test
    void testProducer() {
        rabbitTemplate.convertAndSend("test.exchange", "test.test", "hello world");
    }

    @Test
    @RabbitListener(queues = "test.queue")
    void testConsumer(Message message) throws UnsupportedEncodingException {
        System.out.println(new String(message.getBody(), "UTF-8"));
    }

    @Test
    @RabbitListener(queues = MQConfig.ORDER_RELEASE_ORDER_QUEUE)
    void testOrderDelay(Channel channel, Message message){
        System.out.println(message.getBody());
    }
}
