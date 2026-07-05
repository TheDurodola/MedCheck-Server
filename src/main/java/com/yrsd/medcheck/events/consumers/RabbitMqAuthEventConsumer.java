package com.yrsd.medcheck.events.consumers;

import com.yrsd.medcheck.events.UserRegisteredEvent;
import com.yrsd.medcheck.exceptions.EmailDeliveryException;
import com.yrsd.medcheck.proxy.email.EmailClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class RabbitMqAuthEventConsumer  implements AuthEventConsumer {

    private final EmailClient emailClient;

    @Override
    @RabbitListener( queues = {"${RABBITMQ_QUEUE_NAME}"})
    public void listen(UserRegisteredEvent event) {

        try {
            log.info("Processing UserRegisteredEvent for {}", event.email());
            emailClient.sendWelcomeEmail(event);
        } catch (EmailDeliveryException e) {
            log.error("Failed to send welcome email to {}", event.email(), e);
            throw new AmqpRejectAndDontRequeueException("Email failed to send " + e);
        }

    }
}
