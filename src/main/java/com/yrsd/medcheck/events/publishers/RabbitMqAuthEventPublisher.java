package com.yrsd.medcheck.events.publishers;

import com.yrsd.medcheck.events.UserRegisteredEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RabbitMqAuthEventPublisher implements AuthEventPublisher {

    private final RabbitTemplate rabbitTemplate;


    @Value("${RABBITMQ_EXCHANGE_NAME}")
    private  String EXCHANGE_NAME;

    @Value("${RABBITMQ_USER_REGISTERED_ROUTING_KEY}")
    private  String RABBITMQ_USER_REGISTERED_ROUTING_KEY;

    @Override
    public void WelcomeEmailEvent(UserRegisteredEvent event) {
        rabbitTemplate.convertAndSend(EXCHANGE_NAME, RABBITMQ_USER_REGISTERED_ROUTING_KEY, event);
    }
}
