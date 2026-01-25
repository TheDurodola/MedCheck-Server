package com.yrsd.medcheck.dtos.requests;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
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

    @NotBlank(message = "Shelf Life is required")

    private Duration shelfLife;


}
