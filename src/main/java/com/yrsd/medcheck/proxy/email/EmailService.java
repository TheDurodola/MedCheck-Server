package com.yrsd.medcheck.proxy.email;


import com.yrsd.medcheck.events.UserRegisteredEvent;
import jakarta.validation.constraints.Email;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;

public interface EmailService {


    public void sendWelcomeEmail(UserRegisteredEvent event);
}
