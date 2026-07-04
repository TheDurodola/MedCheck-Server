package com.yrsd.medcheck.dtos.requests;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class DenyReportRequest {
    private String investigatorId;
    private String reportId;
}
