package com.yrsd.medcheck.controllers;

import com.yrsd.medcheck.dtos.responses.AssignedBatchesResponse;
import com.yrsd.medcheck.services.interfaces.BatchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@Slf4j
@RestController
@RequestMapping({"/api/v1/batch", "/batch"})
@RequiredArgsConstructor
public class BatchController {

    private final BatchService batchService;

    @GetMapping({"", "/", "/assigned"})
    public ResponseEntity<AssignedBatchesResponse> getAssignedBatches(Authentication authentication) {
        String username = Objects.requireNonNull(authentication.getPrincipal()).toString();
        log.info("Fetching assigned batches for employee: {}", username);
        AssignedBatchesResponse response = batchService.getBatchesAssignedToOrganisation(username);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
