package com.yrsd.medcheck.proxy.cloud;


import com.yrsd.medcheck.dtos.requests.RegisterUserRequest;
import com.yrsd.medcheck.dtos.responses.CloudServiceResponse;
import com.yrsd.medcheck.exceptions.FailedFileUploadException;

public interface CloudService {
    CloudServiceResponse uploadProfilePicture(RegisterUserRequest request) throws FailedFileUploadException;
}
