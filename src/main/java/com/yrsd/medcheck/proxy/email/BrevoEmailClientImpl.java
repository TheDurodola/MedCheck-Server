package com.yrsd.medcheck.proxy.email;

import brevo.ApiException;
import brevoApi.TransactionalEmailsApi;
import brevoModel.CreateSmtpEmail;
import brevoModel.SendSmtpEmail;
import brevoModel.SendSmtpEmailSender;
import brevoModel.SendSmtpEmailTo;
import com.yrsd.medcheck.events.UserRegisteredEvent;
import com.yrsd.medcheck.exceptions.EmailDeliveryException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class BrevoEmailClientImpl implements EmailClient {

    private final TransactionalEmailsApi transactionalEmailsApi;
    private final SendSmtpEmailSender sendSmtpEmailSender;


    @Override
    public void sendWelcomeEmail(UserRegisteredEvent event) throws EmailDeliveryException {

        log.info("Send Welcome Email Consumer hit");

        SendSmtpEmail email = new SendSmtpEmail();
        email.setSender(sendSmtpEmailSender);
        email.to(List.of(new SendSmtpEmailTo().email(event.email())));
        email.setSubject("Welcome to MedCheck!");
        email.setHtmlContent("<p>Hi " + event.firstName() + ", welcome aboard!</p>");

        try {
            CreateSmtpEmail createSmtpEmail = transactionalEmailsApi.sendTransacEmail(email);
            log.info("SMTP Email {} has been sent", createSmtpEmail);
        } catch (ApiException e) {
            log.error("Failed to send welcome email to {}: {}", event.email(), e.getMessage(), e);
            throw new EmailDeliveryException("Brevo send failed", e);
        }

    }
}
