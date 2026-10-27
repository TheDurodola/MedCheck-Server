package com.yrsd.medcheck.dtos;

public record DrugDetail(
    String drugBrandName,
    String genericName,
    String manufacturerName,
    String nafdacRegistrationNumber,
    String description
) {
}