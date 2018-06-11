package com.pmdcodereview.rabbitMQ;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.amqp.support.converter.SimpleMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
public class RabbitMQListener implements MessageListener{

    private final static Logger LOGGER = LoggerFactory.getLogger(RabbitMQListener.class);

    private final MessageConverter jsonMessageConverter;

    public RabbitMQListener(MessageConverter jsonMessageConverter) {
        this.jsonMessageConverter = jsonMessageConverter;
    }

    @Override
    public void onMessage(Message message) {
        LOGGER.info("Message Received: "+ message);
        Object fromMessage = jsonMessageConverter.fromMessage(message);
        LOGGER.info("Message Payload: "+ fromMessage.toString());

    }
}
