package com.pmdcodereview.rabbitMQ;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.amqp.support.converter.SimpleMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component

public class RabbitMQListener {

    @Autowired
    MessageConverter messageConverter = new SimpleMessageConverter();

    @RabbitListener(queues="${pmdReview.rabbitmq.queue}")
    public void receive(Message message) {
        HttpServletRequest httpServletRequest = (HttpServletRequest) messageConverter.fromMessage(message);
        System.out.println("Listener "+message);
    }

}
