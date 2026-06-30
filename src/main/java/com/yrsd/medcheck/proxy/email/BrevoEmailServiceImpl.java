package com.yrsd.medcheck.proxy.email;

import brevo.ApiException;
import brevoApi.TransactionalEmailsApi;
import brevoModel.CreateSmtpEmail;
import brevoModel.SendSmtpEmail;
import brevoModel.SendSmtpEmailSender;
import brevoModel.SendSmtpEmailTo;
import com.yrsd.medcheck.events.UserRegisteredEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class BrevoEmailServiceImpl implements EmailService {



    private final TransactionalEmailsApi transactionalEmailsApi;
    private final SendSmtpEmailSender sendSmtpEmailSender;



    @Override
    @RabbitListener( queues = {"${RABBITMQ_QUEUE_NAME}"})
    public void sendWelcomeEmail(UserRegisteredEvent event) {

        log.info("Send Welcome Email Consumer hit");
        TransactionalEmailsApi apiInstance = new TransactionalEmailsApi();
        apiInstance.setApiClient(transactionalEmailsApi.getApiClient());

        SendSmtpEmail sendSmtpEmail = new SendSmtpEmail();
        sendSmtpEmail.setSender(sendSmtpEmailSender);
        sendSmtpEmail.to(List.of(new SendSmtpEmailTo().email(event.email())));
        sendSmtpEmail.setSubject("Welcome to MedCheck!");
        sendSmtpEmail.setHtmlContent("<p>Hi " + event.firstName() + ", welcome aboard!</p>");

        try {
            CreateSmtpEmail createSmtpEmail = apiInstance.sendTransacEmail(sendSmtpEmail);
            log.info("SMTP Email {} has been sent", createSmtpEmail);
        } catch (ApiException e) {
            log.error("Failed to send welcome email to {}: {}", event.email(), e.getMessage(), e);
            throw new AmqpRejectAndDontRequeueException("Brevo send failed", e);
        }

    }
}
