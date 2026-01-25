package com.yrsd.medcheck.controllers;

import com.yrsd.medcheck.dtos.requests.CreateBatchRequest;
import com.yrsd.medcheck.dtos.requests.CreateDrugRequest;
import com.yrsd.medcheck.dtos.responses.CreateBatchResponse;
import com.yrsd.medcheck.dtos.responses.CreateDrugResponse;
import com.yrsd.medcheck.services.interfaces.BatchService;
import com.yrsd.medcheck.services.interfaces.DrugService;
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
@RequestMapping("/api/v1/manufacturer")
@RequiredArgsConstructor
public class ManufacturerController {
    private final DrugService drugService;
    private final BatchService batchService;


    @PostMapping("/drug")
    public ResponseEntity<CreateDrugResponse> createUser(@Valid @RequestBody CreateDrugRequest request, Authentication authentication) {
        CreateDrugResponse response = drugService.createDrug(request, Objects.requireNonNull(authentication.getPrincipal()).toString());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/batch")
    public ResponseEntity<CreateBatchResponse> createBatch(@Valid @RequestBody CreateBatchRequest request, Authentication authentication) {
        request.setManufacturerId(Objects.requireNonNull(authentication.getPrincipal()).toString());
        CreateBatchResponse response = batchService.createBatch(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
