package com.yrsd.medcheck.services;

import com.yrsd.medcheck.dtos.requests.CreateNewBatchRequest;
import com.yrsd.medcheck.dtos.responses.CreateNewBatchResponse;

public interface BatchService {
    public CreateNewBatchResponse createNewBatch(CreateNewBatchRequest request);
}
