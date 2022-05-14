package com.zyj.paocai.ware.listener;

import com.rabbitmq.client.Channel;
import com.zyj.paocai.common.entity.to.OrderTo;
import com.zyj.paocai.common.entity.to.StockLockedTo;
import com.zyj.paocai.common.exception.BizCodeEnum;
import com.zyj.paocai.ware.config.MQConfig;
import com.zyj.paocai.ware.service.WareSkuService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @author lulx
 * @date 2022-05-11 16:37
 **/
@Slf4j
@Component
@RabbitListener(queues = MQConfig.STOCK_RELEASE_STOCK_QUEUE)
public class StockReleaseListener {

    @Autowired
    WareSkuService wareSkuService;

    /**
     * 库存自动解锁
     *
     * @param to
     */
    @RabbitHandler
    public void handleStockLockedRelease(StockLockedTo to, Message message, Channel channel) throws IOException {
        try {
            log.info("库存工作单:{}，解锁库存", to.getId());
            wareSkuService.unlockStock(to);
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (Exception e) {
            log.error(BizCodeEnum.UNLOCK_STOCK_EXCEPTION.getMsg() + ":", e.getMessage());
            //消息拒绝以后,重新入队让其他继续解锁
            channel.basicReject(message.getMessageProperties().getDeliveryTag(), true);
        }
    }

    @RabbitHandler
    public void handleOrderCloseRelease(OrderTo order, Message message, Channel channel) throws IOException {
        try {
            log.info("订单关闭准备解锁库存：{}", order.getOrderSn());
            wareSkuService.unlockStock(order);
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (Exception e) {
            log.error(BizCodeEnum.UNLOCK_STOCK_EXCEPTION.getMsg() + ":", e.getMessage());
            channel.basicReject(message.getMessageProperties().getDeliveryTag(), true);
        }
    }
}
