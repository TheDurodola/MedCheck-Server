package com.yrsd.medcheck.controllers;

import com.yrsd.medcheck.dtos.responses.AssignedBatchesResponse;
import com.yrsd.medcheck.dtos.responses.DrugWithBatchesResponse;
import com.yrsd.medcheck.services.interfaces.BatchService;
import com.yrsd.medcheck.services.interfaces.OrganisationService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RetailerControllerTest {

    @Mock
    private BatchService batchService;

    @Mock
    private OrganisationService organisationService;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private RetailerController retailerController;

    @Test
    @DisplayName("Retailer getAssignedBatches returns 200 OK")
    void getAssignedBatches_Success() {
        String username = "retailer-user";
        when(authentication.getPrincipal()).thenReturn(username);

        AssignedBatchesResponse serviceResponse = new AssignedBatchesResponse(List.of(new DrugWithBatchesResponse()));
        when(batchService.getBatchesAssignedToOrganisation(username)).thenReturn(serviceResponse);

        ResponseEntity<AssignedBatchesResponse> response = retailerController.getAssignedBatches(authentication);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().getDrugs().size());
        verify(batchService, times(1)).getBatchesAssignedToOrganisation(username);
    }
}
