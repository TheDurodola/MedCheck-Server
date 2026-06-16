package com.yrsd.medcheck.proxy.email;


import jakarta.validation.constraints.Email;
import org.springframework.amqp.rabbit.annotation.RabbitListener;

public interface EmailService {
    public void sendWelcomeEmail(Email email);
}
