package com.yrsd.medcheck.proxy.paymentGateway;

import com.yrsd.medcheck.dtos.requests.CreateTransferRecipientsRequest;
import com.yrsd.medcheck.dtos.requests.CreateTransferRecipientsResponse;
import com.yrsd.medcheck.dtos.requests.InitiatePaymentRequest;
import com.yrsd.medcheck.dtos.requests.PaymentVerificationResponse;
import com.yrsd.medcheck.dtos.responses.InitiatePaymentResponse;
import com.yrsd.medcheck.dtos.responses.ListAllBanksResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;


@Component

public class PaystackPaymentGatewayImpl implements PaymentGatewayClient {

    private final RestClient client;


    public PaystackPaymentGatewayImpl() {
         client = RestClient.builder()
                 .baseUrl("https://api.paystack.co")
                 .defaultHeader("Accept", "application/json")
                 .build();
    }



    @Override
    public ListAllBanksResponse listAllBanks() {
        return null;
    }



    //            "type": "nuban",
    //            "name": "John Doe",
    //            "account_number": "0001234567",
    //            "bank_code": "058",

    //            "currency": "NGN"


    @Override
    public CreateTransferRecipientsResponse createTransferRecipients(CreateTransferRecipientsRequest request) {
        RestClient.RequestHeadersSpec<?> uri = client.get()
                .uri("/bank?currency=NGN");

        return null;
    }


//    {
//            "source": "balance",
//            "amount": 100000,
//            "reference": "mdc_9ee55786-2323-4760-98e2-6380c9cb3f68",
//            "recipient": "RCP_c4szlkzm3db8dzm",
//            "reason": "Bonus for the week"
//    }
    @Override
    public InitiatePaymentResponse initiatePayment(InitiatePaymentRequest request) {

        RestClient.RequestBodySpec body = client
                .post()

                .uri("/initiatePayment")
                .body(request);
        return null;
    }


    //TODO: Contemplating if I would be needing this since I would be using webhooks to verify
    // the transaction rather than polling.

    @Override
    public PaymentVerificationResponse verify(String reference) {
        return null;
    }
}
