package com.yrsd.medcheck.controllers;

import com.yrsd.medcheck.dtos.requests.TransferPackRequest;
import com.yrsd.medcheck.dtos.responses.TransferPackResponse;
import com.yrsd.medcheck.services.interfaces.BatchService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@Slf4j
@RestController
@RequestMapping("/api/v1/consumer")
@RequiredArgsConstructor
public class ConsumerController {

    private final BatchService batchService;

    @PostMapping("/consumption/pack")
    public ResponseEntity<TransferPackResponse> consumePack(@Valid @RequestBody TransferPackRequest request, Authentication authentication) {
        request.setSenderId(Objects.requireNonNull(authentication.getPrincipal()).toString());
        TransferPackResponse response = batchService.transferPack(request);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }


}
