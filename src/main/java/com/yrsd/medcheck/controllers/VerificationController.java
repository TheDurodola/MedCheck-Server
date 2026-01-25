package com.yrsd.medcheck.controllers;

import com.yrsd.medcheck.dtos.requests.VerifyBatchRequest;
import com.yrsd.medcheck.dtos.requests.VerifyPackRequest;
import com.yrsd.medcheck.dtos.requests.VerifySachetRequest;
import com.yrsd.medcheck.dtos.responses.VerifyBatchResponse;
import com.yrsd.medcheck.dtos.responses.VerifyPackResponse;
import com.yrsd.medcheck.dtos.responses.VerifySachetResponse;
import com.yrsd.medcheck.services.interfaces.BatchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/verification")
@RequiredArgsConstructor
public class VerificationController {

    private final BatchService batchService;

    @PostMapping("/batch/{id}")
    public ResponseEntity<VerifyBatchResponse> verifyBatch(@PathVariable String id) {
        VerifyBatchRequest request = new VerifyBatchRequest();
        request.setBatchVerificationCode(id);

        VerifyBatchResponse response = batchService.verifyBatch(request);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/pack/{id}")
    public ResponseEntity<VerifyPackResponse> verifyPack(@PathVariable String id) {
        VerifyPackRequest request = new VerifyPackRequest();
        request.setPackVerificationCode(id);

        VerifyPackResponse response = batchService.verifyPack(request);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/sachet/{id}")
    public ResponseEntity<VerifySachetResponse> verifySachet(@PathVariable String id) {
        VerifySachetRequest  request = new VerifySachetRequest();
        request.setSachetVerificationCode(id);
        VerifySachetResponse response = batchService.verifySachet(request);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}
