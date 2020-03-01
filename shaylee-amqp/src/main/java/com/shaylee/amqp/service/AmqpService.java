package com.shaylee.amqp.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.amqp.core.AmqpTemplate;

/**
 * Title: AMQP服务类
 * Project: shaylee-common
 *
 * @author Adrian
 * @date 2020-03-01
 */
public interface AmqpService {

    /**
     * 获取AmqpTemplate
     *
     * @return AmqpTemplate
     */
    AmqpTemplate getAmqpTemplate();

    /**
     * 发送消息到队列中
     *
     * @param queueName 队列名
     * @param message 消息内容
     */
    void sendMessage(String queueName, Object message) throws JsonProcessingException;

    /**
     * 发送消息到队列中
     *
     * @param exchange 交换器名
     * @param routingKey 路由键
     * @param message 消息内容
     */
    void sendMessage(String exchange, String routingKey, Object message) throws JsonProcessingException;

    /**
     * 发送消息到交换器中(交换器可以绑定多个队列)
     *
     * @param exchange 交换器名
     * @param message 消息内容
     */
    void sendMessageToExchange(String exchange, Object message) throws JsonProcessingException;
}
