package com.yrsd.medcheck.controllers;

import com.yrsd.medcheck.dtos.requests.VerifyBatchRequest;
import com.yrsd.medcheck.dtos.requests.VerifyPackRequest;
import com.yrsd.medcheck.dtos.requests.VerifySachetRequest;
import com.yrsd.medcheck.dtos.responses.VerifyBatchResponse;
import com.yrsd.medcheck.dtos.responses.VerifyPackResponse;
import com.yrsd.medcheck.dtos.responses.VerifySachetResponse;
import com.yrsd.medcheck.services.interfaces.BatchService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/verification")
@RequiredArgsConstructor
public class VerificationController {

    private final BatchService batchService;

    @PutMapping("/batch/{id}")
    public ResponseEntity<VerifyBatchResponse> verifyBatch(@PathVariable String id) {
        VerifyBatchRequest request = new VerifyBatchRequest();
        request.setBatchVerificationCode(id);

        VerifyBatchResponse response = batchService.verifyBatch(request);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }


    @PutMapping("/pack")
    public ResponseEntity<VerifyPackResponse> verifyPack(@RequestBody VerifyPackRequest request) {
        VerifyPackResponse response = batchService.verifyPack(request);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PutMapping("/sachet/{id}")
    public ResponseEntity<VerifySachetResponse> verifySachet(@PathVariable String id) {
        VerifySachetRequest  request = new VerifySachetRequest();
        request.setSachetVerificationCode(id);
        VerifySachetResponse response = batchService.verifySachet(request);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}
