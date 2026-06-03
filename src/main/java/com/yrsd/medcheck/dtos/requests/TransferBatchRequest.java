package com.yrsd.medcheck.dtos.requests;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class TransferBatchRequest {
    private String batchId;
    private String receiverOrganisationId;
    private String senderId;
}
