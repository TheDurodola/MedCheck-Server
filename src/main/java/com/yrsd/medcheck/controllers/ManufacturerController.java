package com.yrsd.medcheck.controllers;

import com.yrsd.medcheck.data.models.enums.OrganisationType;
import com.yrsd.medcheck.dtos.requests.CreateBatchRequest;
import com.yrsd.medcheck.dtos.requests.CreateDrugRequest;
import com.yrsd.medcheck.dtos.requests.TransferBatchRequest;
import com.yrsd.medcheck.dtos.responses.CreateBatchResponse;
import com.yrsd.medcheck.dtos.responses.CreateDrugResponse;
import com.yrsd.medcheck.dtos.responses.GetAllOrganisationByTypeResponse;
import com.yrsd.medcheck.dtos.responses.TransferBatchResponse;
import com.yrsd.medcheck.dtos.responses.AssignedBatchesResponse;
import com.yrsd.medcheck.services.interfaces.BatchService;
import com.yrsd.medcheck.services.interfaces.DrugService;
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
@RequestMapping("/api/v1/manufacturer")
@RequiredArgsConstructor
public class ManufacturerController {
    private final DrugService drugService;
    private final BatchService batchService;
    private final OrganisationService organisationService;

    @GetMapping
    public ResponseEntity<GetAllOrganisationByTypeResponse> getAllManufacturers() {
        return ResponseEntity.status(HttpStatus.OK).body(organisationService.getAllOrganisationsByType(OrganisationType.MANUFACTURE));
    }

    @GetMapping("/batch")
    public ResponseEntity<AssignedBatchesResponse> getAssignedBatches(Authentication authentication) {
        String username = Objects.requireNonNull(authentication.getPrincipal()).toString();
        AssignedBatchesResponse response = batchService.getBatchesAssignedToOrganisation(username);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/drug")
    public ResponseEntity<CreateDrugResponse> createDrug(@Valid @RequestBody CreateDrugRequest request, Authentication authentication) {
        CreateDrugResponse response = drugService.createDrug(request, Objects.requireNonNull(authentication.getPrincipal()).toString());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/batch")
    public ResponseEntity<CreateBatchResponse> createBatch(@Valid @RequestBody CreateBatchRequest request, Authentication authentication) {
        request.setManufacturing_employee_Id(Objects.requireNonNull(authentication.getPrincipal()).toString());
        CreateBatchResponse response = batchService.createBatch(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/distribution/batch")
    public ResponseEntity<TransferBatchResponse>  distributeBatch(@Valid @RequestBody TransferBatchRequest request, Authentication authentication) {
       log.debug("Distributing batch with ID: {} from manufacturer with ID: {} to distributor with ID: {}", request.getBatchId(), authentication.getPrincipal(), request.getReceiverOrganisationId());
        request.setSenderId(Objects.requireNonNull(authentication.getPrincipal()).toString());

        TransferBatchResponse response = batchService.transferBatch(request);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
