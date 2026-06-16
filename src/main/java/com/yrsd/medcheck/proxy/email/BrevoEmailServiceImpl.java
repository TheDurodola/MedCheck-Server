package com.yrsd.medcheck.proxy.email;

import jakarta.validation.constraints.Email;
import org.springframework.stereotype.Service;

@Service
public class BrevoEmailServiceImpl implements EmailService {


    @Override
    public void sendWelcomeEmail(Email email) {

    }
}
