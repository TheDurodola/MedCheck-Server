package com.yrsd.medcheck.dtos.responses;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class TransferBatchResponse {
    private String message;
    private LocalDateTime timestamp;
}
