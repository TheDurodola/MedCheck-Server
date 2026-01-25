package com.yrsd.medcheck.dtos.requests;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NegativeOrZero;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

import java.time.Duration;

@Setter
@Getter
public class CreateDrugRequest {
    @NotBlank(message = "Brand name is required")
    private String brandName;

    @NotBlank(message = "Generic name is required")
    private String genericName;

    @NotBlank(message = "NAFDAC Registration Number is required")
    private String nafdacRegistrationNumber;

    private String description;

    @Positive(message = "Expiration must be a positive value")
    private Integer expirationDurationInDays;


}
