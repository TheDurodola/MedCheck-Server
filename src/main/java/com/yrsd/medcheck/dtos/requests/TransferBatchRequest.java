package com.yrsd.medcheck.dtos.requests;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TransferBatchRequest {
    private String batchId;
    private String receiverId;
    private String senderId;
}
