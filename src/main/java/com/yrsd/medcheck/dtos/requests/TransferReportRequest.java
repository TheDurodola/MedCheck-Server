package com.yrsd.medcheck.dtos.requests;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class TransferReportRequest {
    private String senderId;
    private String recipientId;
    private String reportId;
}
