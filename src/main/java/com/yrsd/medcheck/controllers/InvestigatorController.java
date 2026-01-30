package com.yrsd.medcheck.controllers;

import com.yrsd.medcheck.dtos.requests.CreateOrganisationRequest;
import com.yrsd.medcheck.dtos.responses.CreateOrganisationResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1/investigator")
@RequiredArgsConstructor
public class InvestigatorController {


    @GetMapping("/organisation")
    public ResponseEntity<CreateOrganisationResponse> getAllOrganisation(CreateOrganisationRequest request){
        return null;
    }

    @PutMapping("/user/unsuspend")
    public ResponseEntity<CreateOrganisationResponse> unsuspendUser(CreateOrganisationRequest request){
        return null;
    }

    @PutMapping("/user/suspend")
    public ResponseEntity<CreateOrganisationResponse> suspendUser(CreateOrganisationRequest request){
        return null;
    }

    @GetMapping("/user")
    public ResponseEntity<CreateOrganisationResponse> getAllUsers(CreateOrganisationRequest request){
        return null;
    }

}
