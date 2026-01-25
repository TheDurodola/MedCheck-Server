package com.yrsd.medcheck.dtos.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CreateBatchRequest {

    @NotBlank
    @Size(min = 1)
    private long amountOfBatches;

    @NotBlank
    @Size(min = 1)
    private long amountOfPacks;

    @NotBlank
    @Size(min = 1)
    private long amountOfSachets;
    @NotBlank
    private String drugId;
    @NotBlank
    private String manufacturerId;
}
