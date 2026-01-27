package com.yrsd.medcheck.dtos.requests;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SellPackRequest {
    private String packId;
    private String retailerId;
}