package com.yrsd.medcheck.proxy.email;


import jakarta.validation.constraints.Email;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;

public interface EmailService {


    public void sendWelcomeEmail(Email email);
}
