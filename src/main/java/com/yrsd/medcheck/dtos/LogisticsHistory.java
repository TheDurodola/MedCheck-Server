package com.yrsd.medcheck.dtos;

import java.time.Instant;

public record LogisticsHistory (ProductType productType, String sender,
                                String recipient, Instant timestamp){

}
