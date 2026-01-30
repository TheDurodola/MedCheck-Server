package com.yrsd.medcheck.dtos.requests;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CreateOrganisationRequest {
    @NotBlank(message = "Organisation name is required")
    private String name;

    @NotBlank(message = "Enter a valid organisation type")
    private String organizationType;
}
