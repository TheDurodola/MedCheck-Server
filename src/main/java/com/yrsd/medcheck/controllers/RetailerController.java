package com.yrsd.medcheck.controllers;

import com.yrsd.medcheck.data.models.enums.OrganisationType;
import com.yrsd.medcheck.dtos.requests.TransferPackRequest;
import com.yrsd.medcheck.dtos.responses.GetAllOrganisationByTypeResponse;
import com.yrsd.medcheck.dtos.responses.TransferPackResponse;
import com.yrsd.medcheck.dtos.responses.AssignedBatchesResponse;
import com.yrsd.medcheck.services.interfaces.BatchService;
import com.yrsd.medcheck.services.interfaces.OrganisationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@Slf4j
@RestController
@RequestMapping("/api/v1/retailer")
@RequiredArgsConstructor
public class RetailerController {

    private final BatchService batchService;
    private final OrganisationService organisationService;


    @GetMapping
    public ResponseEntity<GetAllOrganisationByTypeResponse> getAllRetailers() {
        log.debug("Fetching all retailers");
        return ResponseEntity.status(HttpStatus.OK).body(organisationService.getAllOrganisationsByType(OrganisationType.RETAIL));
    }

    @GetMapping("/batch")
    public ResponseEntity<AssignedBatchesResponse> getAssignedBatches(Authentication authentication) {
        String username = Objects.requireNonNull(authentication.getPrincipal()).toString();
        AssignedBatchesResponse response = batchService.getBatchesAssignedToOrganisation(username);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/distribution/pack")
    public ResponseEntity<TransferPackResponse> distributePack(@Valid @RequestBody TransferPackRequest request, Authentication authentication) {
        request.setSenderId(Objects.requireNonNull(authentication.getPrincipal()).toString());
        TransferPackResponse response = batchService.transferPack(request);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
