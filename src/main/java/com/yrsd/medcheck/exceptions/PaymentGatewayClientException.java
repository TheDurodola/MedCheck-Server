package com.yrsd.medcheck.exceptions;

import java.io.IOException;

public class PaymentGatewayClientException extends IOException {
    public PaymentGatewayClientException(int value, String body) {
        super(body);
    }
}
