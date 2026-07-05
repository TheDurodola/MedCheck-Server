package com.yrsd.medcheck.dtos.responses;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class CreateReportResponse {
    private String message;
}
