package com.yrsd.medcheck.controllers;

import com.yrsd.medcheck.dtos.requests.CreateOrganisationRequest;
import com.yrsd.medcheck.dtos.responses.CreateOrganisationResponse;
import com.yrsd.medcheck.services.interfaces.OrganisationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
public class AdminController {

    private final OrganisationService organisationService;

    @PostMapping("/organisation")
    public ResponseEntity<CreateOrganisationResponse> createOrganisation(@RequestBody CreateOrganisationRequest request){
        CreateOrganisationResponse response = organisationService.createOrganisation(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


    @DeleteMapping("/user")
    public ResponseEntity<CreateOrganisationResponse> deleteUser(CreateOrganisationRequest request){
        return null;
    }


    @DeleteMapping("/organisation")
    public ResponseEntity<CreateOrganisationResponse> deleteOrganisation(CreateOrganisationRequest request){
        return null;
    }

    @GetMapping("/organisation")
    public ResponseEntity<CreateOrganisationResponse> getAllOrganisation(CreateOrganisationRequest request){
        return null;
    }

    @PutMapping("/user/suspend")
    public ResponseEntity<CreateOrganisationResponse> unsuspendUser(CreateOrganisationRequest request){
        return null;
    }

    @PutMapping("/user/unsuspend")
    public ResponseEntity<CreateOrganisationResponse> suspendUser(CreateOrganisationRequest request){
        return null;
    }

    @GetMapping("/user")
    public ResponseEntity<CreateOrganisationResponse> getAllUsers(CreateOrganisationRequest request){
        return null;
    }

}
