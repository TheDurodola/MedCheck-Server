package com.yrsd.medcheck.dtos.responses;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;
import java.util.Set;

@Setter
@Getter
public class CreateBatchResponse {
    private String message;
    private Map<String, Object> drug;
    private Set<String> batch;
    private Set<String> pack;
    private Set<String> sachet;
}
