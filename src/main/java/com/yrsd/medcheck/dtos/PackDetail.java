package com.yrsd.medcheck.dtos;


import java.time.LocalDate;

public record PackDetail(
    LocalDate expiryDate,
    LocalDate manufactureDate
) {
}
