package com.yrsd.medcheck.services.interfaces;

import com.yrsd.medcheck.dtos.requests.CreateBatchRequest;
import com.yrsd.medcheck.dtos.requests.VerifyBatchRequest;
import com.yrsd.medcheck.dtos.requests.VerifyPackRequest;
import com.yrsd.medcheck.dtos.requests.VerifySachetRequest;
import com.yrsd.medcheck.dtos.responses.CreateBatchResponse;
import com.yrsd.medcheck.dtos.responses.VerifyBatchResponse;
import com.yrsd.medcheck.dtos.responses.VerifyPackResponse;
import com.yrsd.medcheck.dtos.responses.VerifySachetResponse;

public interface BatchService {
    CreateBatchResponse createBatch(CreateBatchRequest request);

    VerifyBatchResponse verifyBatch(VerifyBatchRequest request);
    VerifyPackResponse verifyPack(VerifyPackRequest request);
    VerifySachetResponse verifySachet(VerifySachetRequest request);
}