package com.yrsd.medcheck.dtos.responses;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
public class CreateDrugResponse {
    private String id;
    private String drugCode;
    private Instant createdDate;
}
