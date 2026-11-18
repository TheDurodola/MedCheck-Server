package com.yrsd.medcheck.controllers;

import com.yrsd.medcheck.dtos.responses.AssignedBatchesResponse;
import com.yrsd.medcheck.dtos.responses.BatchResponse;
import com.yrsd.medcheck.dtos.responses.DrugWithBatchesResponse;
import com.yrsd.medcheck.services.interfaces.BatchService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import java.math.BigInteger;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BatchControllerTest {

    @Mock
    private BatchService batchService;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private BatchController batchController;

    @Test
    @DisplayName("BatchController getAssignedBatches returns 200 OK with batches grouped by drug")
    void getAssignedBatches_Success() {
        String username = "test-employee";
        when(authentication.getPrincipal()).thenReturn(username);

        BatchResponse batchResponse = BatchResponse.builder()
                .id("batch-1")
                .batchIdentifier("BAT-01")
                .verificationCode("VER-01")
                .manufactureDate(LocalDate.now())
                .expirationDate(LocalDate.now().plusYears(1))
                .verificationCount(BigInteger.ZERO)
                .noOfPacks(10)
                .created(Instant.now())
                .build();

        DrugWithBatchesResponse drugResponse = DrugWithBatchesResponse.builder()
                .drugId("drug-1")
                .drugBrandName("Panadol")
                .drugGenericName("Paracetamol")
                .drugManufacturerName("Pfizer NG")
                .drugNafdacNumber("A4-1234")
                .drugDescription("Pain reliever")
                .batches(List.of(batchResponse))
                .build();

        AssignedBatchesResponse serviceResponse = new AssignedBatchesResponse(List.of(drugResponse));
        when(batchService.getBatchesAssignedToOrganisation(username)).thenReturn(serviceResponse);

        ResponseEntity<AssignedBatchesResponse> response = batchController.getAssignedBatches(authentication);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().getDrugs().size());

        DrugWithBatchesResponse drug = response.getBody().getDrugs().getFirst();
        assertEquals("drug-1", drug.getDrugId());
        assertEquals("Panadol", drug.getDrugBrandName());
        assertEquals(1, drug.getBatches().size());
        assertEquals("batch-1", drug.getBatches().getFirst().getId());
        verify(batchService, times(1)).getBatchesAssignedToOrganisation(username);
    }
}
