package com.yrsd.medcheck.dtos.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CreateBatchRequest {

    @Positive
    private long amountOfBatches;

    @Positive
    private long amountOfPacks;

    @Positive
    private long amountOfSachets;

    @NotBlank
    private String drugId;


    private String manufacturing_employee_Id;
}
