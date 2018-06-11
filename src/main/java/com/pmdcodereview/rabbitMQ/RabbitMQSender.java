package com.pmdcodereview.rabbitMQ;

import com.pmdcodereview.model.MessageBroker;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Service
public class RabbitMQSender {

    @Autowired
    private AmqpTemplate rabbitTemplate;

    @Value("${pmdReview.rabbitmq.exchange}")
    private String exchange;

    @Value("${pmdReview.rabbitmq.routingkey}")
    private String routingkey;

    public void send(HttpServletResponse response, HttpServletRequest request) {
        rabbitTemplate.convertAndSend(request.getCookies());
    }
}
