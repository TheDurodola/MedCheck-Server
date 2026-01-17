package com.yrsd.medcheck.services;

import com.yrsd.medcheck.dtos.requests.RegisterUserRequest;
import com.yrsd.medcheck.dtos.responses.RegisterUserResponse;

public interface AuthService {
    public RegisterUserResponse registerUser(RegisterUserRequest request);
}
