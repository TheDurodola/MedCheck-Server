package com.yrsd.medcheck.dtos.responses;

import lombok.*;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DrugWithBatchesResponse {
    private String drugId;
    private String drugBrandName;
    private String drugGenericName;
    private String drugManufacturerName;
    private String drugNafdacNumber;
    private String drugDescription;
    private List<BatchResponse> batches;
}
