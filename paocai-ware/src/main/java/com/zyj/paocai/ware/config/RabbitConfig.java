package com.zyj.paocai.ware.config;

import org.springframework.amqp.core.ReturnedMessage;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Configuration;

/**
 * Rabiit配置
 *
 * @author lulx
 * @date 2022-05-11 11:34
 **/
@Configuration
public class RabbitConfig {

    RabbitConfig(RabbitTemplate rabbitTemplate) {
        // 消息转换
//        rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
        // 服务收到消息就回调
        rabbitTemplate.setConfirmCallback((CorrelationData correlationData, boolean ack, String cause) -> {
            // TODO 消息确认处理
            /**
             * 1、做好消息确认机制，手动ack
             * 2、每一个发送的消息都在数据库做好记录。定期将失败的消息再次发送至MQ
             */

        });
        //消息正确抵达队列进行回调
        rabbitTemplate.setReturnsCallback((ReturnedMessage returnedMessage)->{

        });
    }
}
