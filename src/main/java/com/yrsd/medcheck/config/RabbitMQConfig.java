package com.yrsd.medcheck.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Value("${RABBITMQ_EXCHANGE_NAME}")
    private  String EXCHANGE_NAME;

    @Value("${RABBITMQ_QUEUE_NAME}")
    private String QUEUE_NAME;

    @Value("${RABBITMQ_USER_REGISTERED_ROUTING_KEY}")
    private  String RABBITMQ_USER_REGISTERED_ROUTING_KEY;


    @Bean
    public Queue queue() {
        return new Queue(QUEUE_NAME);
    }

    @Bean
    public Exchange exchange() {
        return new TopicExchange(EXCHANGE_NAME);
    }

    @Bean
    public Binding binding(Queue queue, Exchange exchange) {
        return BindingBuilder
                .bind(queue)
                .to(exchange)
                .with(RABBITMQ_USER_REGISTERED_ROUTING_KEY)
                .noargs();
    }

    @Bean
    public MessageConverter messageConverter() {
        return new JacksonJsonMessageConverter();
    }


}
