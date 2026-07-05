package com.yrsd.medcheck.proxy.paymentGateway;

import com.yrsd.medcheck.dtos.requests.CreateTransferRecipientsRequest;
import com.yrsd.medcheck.dtos.requests.CreateTransferRecipientsResponse;
import com.yrsd.medcheck.dtos.requests.InitiatePaymentRequest;
import com.yrsd.medcheck.dtos.requests.PaymentVerificationResponse;
import com.yrsd.medcheck.dtos.responses.InitiatePaymentResponse;
import com.yrsd.medcheck.dtos.responses.ListAllBanksResponse;

public interface PaymentGatewayClient {
    ListAllBanksResponse listAllBanks();
    CreateTransferRecipientsResponse createTransferRecipients(CreateTransferRecipientsRequest request);
    InitiatePaymentResponse initiatePayment(InitiatePaymentRequest request);
    PaymentVerificationResponse verify(String reference);
}
