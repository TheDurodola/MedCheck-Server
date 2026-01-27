package com.yrsd.medcheck.services.interfaces;

import com.yrsd.medcheck.dtos.requests.*;
import com.yrsd.medcheck.dtos.responses.*;

public interface BatchService {
    CreateBatchResponse createBatch(CreateBatchRequest request);

    TransferBatchResponse transferBatch(TransferBatchRequest request);
    TransferPackResponse transferPack(TransferPackRequest request);


    VerifyBatchResponse verifyBatch(VerifyBatchRequest request);
    VerifyPackResponse verifyPack(VerifyPackRequest request);
    VerifySachetResponse verifySachet(VerifySachetRequest request);
}