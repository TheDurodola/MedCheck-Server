package com.yrsd.medcheck.exceptions;

import brevo.ApiException;

public class EmailDeliveryException extends Throwable {
    public EmailDeliveryException(String brevoSendFailed, ApiException e) {
    }
}
