package com.yrsd.medcheck.config;

import brevo.ApiClient;
import brevo.auth.ApiKeyAuth;
import brevoApi.TransactionalEmailsApi;
import brevoModel.SendSmtpEmailSender;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BrevoConfig {

    @Value("${BREVO_API_KEY}")
    private String apiKey;

    @Value("${BREVO_EMAIL_SENDER}")
    private String emailSender;


    @Bean
    public TransactionalEmailsApi transactionalEmailsApi() {
        ApiClient client = brevo.Configuration.getDefaultApiClient();
        ApiKeyAuth auth = (ApiKeyAuth) client.getAuthentication("api-key");
        auth.setApiKey(apiKey);
        TransactionalEmailsApi api = new TransactionalEmailsApi();
        api.setApiClient(client);
        return api;
    }

    @Bean
    public SendSmtpEmailSender brevoSender() {
        return new SendSmtpEmailSender()
                .email(emailSender)
                .name("MedCheck");
    }
}
