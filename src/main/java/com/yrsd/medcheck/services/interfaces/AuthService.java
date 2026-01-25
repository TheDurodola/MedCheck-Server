package com.yrsd.medcheck.services.interfaces;

import com.yrsd.medcheck.dtos.requests.RegisterUserRequest;
import com.yrsd.medcheck.dtos.responses.RegisterUserResponse;
import com.yrsd.medcheck.exceptions.FailedFileUploadException;

public interface AuthService {
    public RegisterUserResponse registerUser(RegisterUserRequest request) throws FailedFileUploadException;

}
