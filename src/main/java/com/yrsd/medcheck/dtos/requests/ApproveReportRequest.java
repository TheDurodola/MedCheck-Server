package com.yrsd.medcheck.dtos.requests;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApproveReportRequest {
    private String investigatorId;
    private String reportId;
}
