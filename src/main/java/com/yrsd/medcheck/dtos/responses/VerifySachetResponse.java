package com.yrsd.medcheck.dtos.responses;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;
import java.util.Set;

@Setter
@Getter
public class VerifySachetResponse {
    private Map<String, String> drug;
    private Map<String, String> pack;
    private Map<String, String> sachet;
    private Set<String> history;
}
