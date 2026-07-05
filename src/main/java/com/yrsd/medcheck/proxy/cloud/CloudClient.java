package com.yrsd.medcheck.proxy.cloud;


import com.yrsd.medcheck.dtos.requests.UploadProfilePictureRequest;
import com.yrsd.medcheck.dtos.requests.UploadReportImage;
import com.yrsd.medcheck.dtos.responses.CloudServiceResponse;
import com.yrsd.medcheck.exceptions.FailedFileUploadException;

public interface CloudClient {
    CloudServiceResponse uploadProfilePicture(UploadProfilePictureRequest request) throws FailedFileUploadException;
    CloudServiceResponse uploadReportPicture(UploadReportImage request) throws FailedFileUploadException;
}
