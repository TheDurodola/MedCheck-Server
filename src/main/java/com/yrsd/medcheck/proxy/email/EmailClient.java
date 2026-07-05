package com.yrsd.medcheck.proxy.email;


import com.yrsd.medcheck.events.UserRegisteredEvent;
import com.yrsd.medcheck.exceptions.EmailDeliveryException;

public interface EmailClient {
    public void sendWelcomeEmail(UserRegisteredEvent event) throws EmailDeliveryException;
}
