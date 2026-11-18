package com.yrsd.medcheck.dtos.responses;

import lombok.*;

import java.math.BigInteger;
import java.time.Instant;
import java.time.LocalDate;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BatchResponse {
    private String id;
    private String batchIdentifier;
    private String verificationCode;
    private LocalDate manufactureDate;
    private LocalDate expirationDate;
    private BigInteger verificationCount;
    private int noOfPacks;
    private Instant created;
}
