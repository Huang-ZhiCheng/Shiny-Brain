package com.hzc.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author : hzc
 * @date: 2022/4/18 - 04 - 18 - 19:03
 * @Description: com.hzc.config
 * @version: 1.0
 */
/*
 * @Description: 配置rabbitMQ类
 */
@Configuration
public class RabbitMQTopicConfig {
    private static final String QUEUE = "seckillQueue";
    private static final String EXCHANGE = "seckillExchange";
    /*
     * @Description: 注入rabbitMQ队列对象到Bean工厂中
     */
    @Bean
    public Queue queue(){
        return new Queue(QUEUE);
    }
    /*
     * @Description:交换机,所有的消息都先发给交换机，再由交换机发送给不同的队列
     */
    @Bean
    public TopicExchange topicExchange(){
        return new TopicExchange(EXCHANGE);
    }
    /*
     * @Description: 队列绑定交换机,且定义交换机发送到该队列的格式
     */
    @Bean
    public Binding binding(){
        return BindingBuilder.bind(queue()).to(topicExchange()).with("seckill.#");
    }
}
