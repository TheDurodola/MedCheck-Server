package com.yrsd.medcheck.services;

import com.yrsd.medcheck.data.models.*;
import com.yrsd.medcheck.data.repositories.*;
import com.yrsd.medcheck.dtos.requests.CreateBatchRequest;
import com.yrsd.medcheck.dtos.requests.VerifyBatchRequest;
import com.yrsd.medcheck.dtos.requests.VerifyPackRequest;
import com.yrsd.medcheck.dtos.requests.VerifySachetRequest;
import com.yrsd.medcheck.dtos.responses.CreateBatchResponse;
import com.yrsd.medcheck.dtos.responses.VerifyBatchResponse;
import com.yrsd.medcheck.dtos.responses.VerifyPackResponse;
import com.yrsd.medcheck.dtos.responses.VerifySachetResponse;
import com.yrsd.medcheck.exceptions.*;
import com.yrsd.medcheck.utils.CodeGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigInteger;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BatchServiceImplTest {

    @Spy
    CodeGenerator codeGenerator;

    @Mock
    UserAccounts userAccounts;

    @Mock
    Drugs drugs;

    @Mock
    Packs packs;

    @Mock
    Sachets sachets;

    @Mock
    Batches batches;

    @InjectMocks
    private BatchServiceImpl batchService;



    private Drug testDrug;
    private UserAccount manufacturer;
    private Batch testBatch;
    private Pack testPack;
    private Sachet testSachet;

    @BeforeEach
    void setUp() {

        manufacturer = new UserAccount();
        manufacturer.setId("manufacturer-123");
        manufacturer.setUsername("Pfizer-NG");

        testDrug = new Drug();
        testDrug.setId("drug-123");
        testDrug.setBrandName("Panadol");
        testDrug.setGenericName("Paracetamol");
        testDrug.setDrugCode("PANA");
        testDrug.setNafdacRegistrationNumber("A1-2345");
        testDrug.setExpiryDurationInDays(365);
        testDrug.setManufacturer(manufacturer);


        testBatch = new Batch();
        testBatch.setId("batch-123");
        testBatch.setVerificationCode("PANA-B-111");
        testBatch.setVerificationCount(BigInteger.ZERO);
        testBatch.setManufactureDate(LocalDate.now());
        testBatch.setExpirationDate(LocalDate.now().plusDays(365));
        testBatch.setCreated(Instant.now());
        testBatch.setLastModified(Instant.now());
        testBatch.setDrug(testDrug);


        testPack = new Pack();
        testPack.setId("pack-123");
        testPack.setVerificationCode("PANA-P-222");
        testPack.setVerificationCount(BigInteger.ZERO);
        testPack.setBatch(testBatch);
        testPack.setDrug(testDrug);


        testSachet = new Sachet();
        testSachet.setId("sachet-123");
        testSachet.setVerificationCode("PANA-S-333");
        testSachet.setVerificationCount(BigInteger.ZERO);
        testSachet.setPack(testPack);
        testSachet.setDrug(testDrug);


        List<Sachet> sachetList = new ArrayList<>();
        sachetList.add(testSachet);
        testPack.setSachets(sachetList);

        List<Pack> packList = new ArrayList<>();
        packList.add(testPack);
        testBatch.setPacks(packList);
    }

    @Test
    void createBatch_Successful_ShouldCreateHierarchyAndReturnResponse() {
        CreateBatchRequest request = new CreateBatchRequest();
        request.setDrugId(testDrug.getId());
        request.setManufacturerId(manufacturer.getId());
        request.setAmountOfBatches(1);
        request.setAmountOfPacks(1);
        request.setAmountOfSachets(1);

        when(drugs.findById(testDrug.getId())).thenReturn(Optional.of(testDrug));
        when(batches.saveAll(any())).thenAnswer(invocation -> invocation.getArgument(0));

        CreateBatchResponse response = batchService.createBatch(request);

        assertNotNull(response);
        assertEquals(testDrug.getId(), response.getDrug().get("drugId"));
        verify(batches, times(1)).saveAll(anyList());
    }

    @Test
    void createBatch_DrugNotFound_ShouldThrowException() {
        CreateBatchRequest request = new CreateBatchRequest();
        request.setDrugId("unknown-id");

        when(drugs.findById("unknown-id")).thenReturn(Optional.empty());

        assertThrows(DrugDoesntExistException.class, () -> batchService.createBatch(request));
        verify(batches, never()).saveAll(any());
    }

    @Test
    void createBatch_UnauthorizedManufacturer_ShouldThrowException() {
        CreateBatchRequest request = new CreateBatchRequest();
        request.setDrugId(testDrug.getId());
        request.setManufacturerId("wrong-manufacturer-id");

        when(drugs.findById(testDrug.getId())).thenReturn(Optional.of(testDrug));

        assertThrows(UnauthorizedException.class, () -> batchService.createBatch(request));
        verify(batches, never()).saveAll(any());
    }


    @Test
    void verifyBatch_Successful_ShouldIncrementCountAndReturnDetails() {
        VerifyBatchRequest request = new VerifyBatchRequest();
        request.setBatchVerificationCode(testBatch.getVerificationCode());

        when(batches.findByVerificationCode(testBatch.getVerificationCode()))
                .thenReturn(Optional.of(testBatch));

        VerifyBatchResponse response = batchService.verifyBatch(request);

        verify(batches, times(1)).save(testBatch);
        assertEquals(BigInteger.ONE, testBatch.getVerificationCount());
        assertNotNull(response.getBatch());
    }

    @Test
    void verifyBatch_NotFound_ShouldThrowException() {
        VerifyBatchRequest request = new VerifyBatchRequest();
        request.setBatchVerificationCode("invalid-code");

        when(batches.findByVerificationCode(anyString())).thenReturn(Optional.empty());

        assertThrows(BatchDoesntExistException.class, () -> batchService.verifyBatch(request));
    }



    @Test
    void verifyPack_Successful_ShouldIncrementCountAndReturnDetails() {
        VerifyPackRequest request = new VerifyPackRequest();
        request.setPackVerificationCode(testPack.getVerificationCode());

        when(packs.findByVerificationCode(testPack.getVerificationCode()))
                .thenReturn(Optional.of(testPack));

        VerifyPackResponse response = batchService.verifyPack(request);

        verify(packs, times(1)).save(testPack);
        assertEquals(BigInteger.ONE, testPack.getVerificationCount());
        assertEquals(testBatch.getVerificationCode(), response.getBatch().get("batchCode"));
    }

    @Test
    void verifyPack_NotFound_ShouldThrowException() {
        VerifyPackRequest request = new VerifyPackRequest();
        request.setPackVerificationCode("invalid-pack");

        when(packs.findByVerificationCode(anyString())).thenReturn(Optional.empty());

        assertThrows(PackDoesntExistException.class, () -> batchService.verifyPack(request));
    }



    @Test
    void verifySachet_Successful_ShouldIncrementCountAndReturnDetails() {
        VerifySachetRequest request = new VerifySachetRequest();
        request.setSachetVerificationCode(testSachet.getVerificationCode());

        when(sachets.findByVerificationCode(testSachet.getVerificationCode()))
                .thenReturn(Optional.of(testSachet));

        VerifySachetResponse response = batchService.verifySachet(request);

        verify(sachets, times(1)).save(testSachet);
        assertEquals(BigInteger.ONE, testSachet.getVerificationCount());
        assertEquals(testPack.getVerificationCode(), response.getPack().get("packCode"));
    }

    @Test
    void verifySachet_NotFound_ShouldThrowException() {
        VerifySachetRequest request = new VerifySachetRequest();
        request.setSachetVerificationCode("invalid-sachet");

        when(sachets.findByVerificationCode(anyString())).thenReturn(Optional.empty());

        assertThrows(SachetDoesntExistException.class, () -> batchService.verifySachet(request));
    }








}