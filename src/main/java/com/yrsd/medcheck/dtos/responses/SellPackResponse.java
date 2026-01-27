package com.yrsd.medcheck.dtos.responses;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SellPackResponse {
    private String message;
    public SellPackResponse(String message) {
        this.message = message;
    }
}
