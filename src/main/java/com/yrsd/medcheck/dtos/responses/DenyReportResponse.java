package com.yrsd.medcheck.dtos.responses;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class DenyReportResponse {
    private String message;
}
