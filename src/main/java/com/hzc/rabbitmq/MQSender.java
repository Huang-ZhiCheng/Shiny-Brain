package com.hzc.rabbitmq;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author : hzc
 * @date: 2022/4/18 - 04 - 18 - 16:45
 * @Description: com.hzc.rabbitmq
 * @version: 1.0
 */
/*
 * @Description:生成者类
 */
@Service
@Slf4j
public class MQSender {
    @Autowired
    private RabbitTemplate rabbitTemplate;
    /*
     * @Description: 发送秒杀信息
     */
    public void sendSeckillMessage(String message){
        log.info(message);
        //发送秒杀信息
        rabbitTemplate.convertAndSend("seckillExchange","seckill.message",message);
    }
}
