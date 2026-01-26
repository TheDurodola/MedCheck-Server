package com.yrsd.medcheck.dtos.requests;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TransferPackRequest {
    private String packId;
    private String receiverId;
    private String senderId;
}
