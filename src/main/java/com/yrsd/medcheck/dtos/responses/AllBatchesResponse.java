package com.yrsd.medcheck.dtos.responses;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class AllBatchesResponse {
    private List<List<String>> batches;
}
