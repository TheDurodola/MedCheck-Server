package com.yrsd.medcheck.dtos.requests;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CreateReportRequest {
    private String userId;
    private String organisationId;
    private String drugId;
    private String description;
}
