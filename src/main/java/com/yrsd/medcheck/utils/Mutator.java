package com.yrsd.medcheck.utils;

import com.yrsd.medcheck.dtos.requests.RegisterUserRequest;

public class Mutator {
    public static void mutate(RegisterUserRequest request) {
        request.setUsername(request.getUsername().toLowerCase());
        request.setFirstName(request.getFirstName().toLowerCase());
        request.setLastName(request.getLastName().toLowerCase());
        request.setEmail(request.getEmail().toLowerCase());
        request.setMiddleName(request.getMiddleName().toLowerCase());
    }
}
