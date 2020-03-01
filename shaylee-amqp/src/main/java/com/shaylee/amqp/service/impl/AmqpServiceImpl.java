package com.shaylee.amqp.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shaylee.amqp.service.AmqpService;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Title:
 * Project: shaylee-common
 *
 * @author Adrian
 * @date 2020-03-01
 */
@Component
public class AmqpServiceImpl implements AmqpService {

    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public AmqpTemplate getAmqpTemplate() {
        return this.rabbitTemplate;
    }

    @Override
    public void sendMessage(String queueName, Object message) throws JsonProcessingException {
        rabbitTemplate.convertAndSend(queueName, objectMapper.writeValueAsString(message));
    }

    @Override
    public void sendMessage(String exchange, String routingKey, Object message) throws JsonProcessingException {
        rabbitTemplate.convertAndSend(exchange, routingKey, objectMapper.writeValueAsString(message));
    }

    @Override
    public void sendMessageToExchange(String exchange, Object message) throws JsonProcessingException {
        rabbitTemplate.convertAndSend(exchange, null, objectMapper.writeValueAsString(message));
    }
}
