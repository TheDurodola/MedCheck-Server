package com.yrsd.medcheck.dtos.responses;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class TransferPackResponse {
    private String status;
    private String message;
    private String sender;
    private String receiver;
    private LocalDateTime timestamp;
}
