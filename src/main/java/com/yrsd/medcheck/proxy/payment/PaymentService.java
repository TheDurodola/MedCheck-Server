package com.yrsd.medcheck.proxy.payment;

import com.yrsd.medcheck.dtos.requests.InitiatePaymentRequest;
import com.yrsd.medcheck.dtos.responses.InitiatePaymentResponse;

public interface PaymentService {
    InitiatePaymentResponse initiatePayment(InitiatePaymentRequest request);
}
