package com.yrsd.medcheck.dtos.requests;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class VerifyBatchRequest {
    @NotBlank
    private String batchVerificationCode;
}
