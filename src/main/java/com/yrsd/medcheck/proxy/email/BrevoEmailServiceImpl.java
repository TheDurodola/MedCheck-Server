//package com.yrsd.medcheck.proxy.email;
//
//import brevo.ApiClient;
//import brevo.ApiException;
//import brevo.Configuration;
//import brevo.auth.ApiKeyAuth;
//import brevo.auth.Authentication;
//import brevoApi.TransactionalEmailsApi;
//import brevoModel.CreateSmtpEmail;
//import brevoModel.SendSmtpEmail;
//import brevoModel.SendSmtpEmailTo;
//import jakarta.validation.constraints.Email;
//import org.springframework.amqp.rabbit.annotation.RabbitListener;
//import org.springframework.stereotype.Service;
//
//@Service
//public class BrevoEmailServiceImpl implements EmailService {
//
//
//
//    @RabbitListener( )
//    @Override
//    public void sendWelcomeEmail(Email email) {
//
//        ApiClient defaultApiClient = Configuration.getDefaultApiClient();
//
//        Authentication authentication = defaultApiClient.getAuthentication("api-key");
//
//        ApiKeyAuth apiKeyAuth = (ApiKeyAuth) authentication;
//        apiKeyAuth.setApiKey("api-key");
//
//        defaultApiClient.setApiKey(apiKeyAuth.getApiKey());
//
//        TransactionalEmailsApi apiInstance = new TransactionalEmailsApi();
//        apiInstance.setApiClient(defaultApiClient);
//
//        SendSmtpEmail sendSmtpEmail = new SendSmtpEmail();
//
//
//
//        try {
//            CreateSmtpEmail createSmtpEmail = apiInstance.sendTransacEmail(sendSmtpEmail);
//        } catch (ApiException e) {
//            throw new RuntimeException(e);
//        }
//
//    }
//}
