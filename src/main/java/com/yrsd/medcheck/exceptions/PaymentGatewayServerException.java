package com.yrsd.medcheck.exceptions;

import java.io.IOException;

public class PaymentGatewayServerException extends IOException {
    public PaymentGatewayServerException(int value, String body) {
        super(body);
    }
}
