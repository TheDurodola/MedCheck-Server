package com.yrsd.medcheck.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AssignedBatchesResponse {
    private List<DrugWithBatchesResponse> drugs;
}
