package com.yrsd.medcheck.services;

import com.yrsd.medcheck.data.models.*;
import com.yrsd.medcheck.data.models.enums.Role;
import com.yrsd.medcheck.data.repositories.*;
import com.yrsd.medcheck.dtos.requests.*;
import com.yrsd.medcheck.dtos.responses.*;
import com.yrsd.medcheck.exceptions.*;
import com.yrsd.medcheck.utils.CodeGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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
    BatchLogisticsRepo batchLogisticsRepo;
    @Mock
    PackLogisticsRepo packLogisticsRepo;
    @Mock
    Sachets sachets;

    @Mock
    Batches batches;

    @InjectMocks
    private BatchServiceImpl batchService;


    private UserAccount testmanufacturer;
    private UserAccount wholesaler;
    private UserAccount retailer;
    private Drug drug;
    private Batch batch;
    private Pack pack;
    private Drug testDrug;
    private UserAccount manufacturer;
    private Batch testBatch;
    private Pack testPack;
    private Sachet testSachet;

    @BeforeEach
    void setUp() {
        testmanufacturer = new UserAccount();
        testmanufacturer.setId("user-1-uuid");
        testmanufacturer.setUsername("Pfizer-NG");
        testmanufacturer.setRole(Role.MANUFACTURER);

        wholesaler = new UserAccount();
        wholesaler.setId("user-2-uuid");
        wholesaler.setUsername("Big-Pharma-Dist");
        wholesaler.setRole(Role.WHOLESALER);

        retailer = new UserAccount();
        retailer.setId("user-3-uuid");
        retailer.setUsername("Olatunde-Pharmacy");
        retailer.setRole(Role.RETAILER);

        drug = new Drug();
        drug.setId("drug-100-uuid");
        drug.setDrugCode("PAN");
        drug.setManufacturer(manufacturer);
        drug.setExpiryDurationInDays(365);

        batch = new Batch();
        batch.setId("batch-uuid");
        batch.setVerificationCode("PAN-B-123");
        batch.setDrug(drug);

        pack = new Pack();
        pack.setId("pack-uuid");
        pack.setVerificationCode("PAN-P-456");
        pack.setBatch(batch);
        pack.setDrug(drug);

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


    @Test
    @DisplayName("Should create batch successfully when manufacturer matches")
    void createBatch_Success() {
        CreateBatchRequest request = new CreateBatchRequest();
        request.setDrugId("drug-100-uuid");
        request.setManufacturerId("Pfizer-NG");
        request.setAmountOfBatches(1);
        request.setAmountOfPacks(1);
        request.setAmountOfSachets(1);

        when(drugs.findById("drug-100-uuid")).thenReturn(Optional.of(drug));
        when(batches.saveAll(any())).thenAnswer(i -> i.getArgument(0));

        CreateBatchResponse response = batchService.createBatch(request);

        assertNotNull(response);
        assertEquals("Batch created", response.getMessage());
        verify(batches, times(1)).saveAll(any());
    }

    @Test
    @DisplayName("Should fail create batch if requester is not the drug manufacturer")
    void createBatch_Unauthorized() {
        CreateBatchRequest request = new CreateBatchRequest();
        request.setDrugId("drug-100-uuid"); // String ID
        request.setManufacturerId("Fake-Manufacturer");

        when(drugs.findById("drug-100-uuid")).thenReturn(Optional.of(drug));

        assertThrows(UnauthorizedException.class, () -> batchService.createBatch(request));
    }

    @Test
    @DisplayName("Manufacturer should transfer to Wholesaler successfully (First Move)")
    void transferBatch_ManufacturerToWholesaler_Success() {
        // String IDs: BatchID, SenderID, ReceiverID
        TransferBatchRequest request = new TransferBatchRequest("batch-uuid", "user-1-uuid", "user-2-uuid");

        when(batches.findById("batch-uuid")).thenReturn(Optional.of(batch));
        when(userAccounts.findById("user-1-uuid")).thenReturn(Optional.of(testmanufacturer));
        when(userAccounts.findById("user-2-uuid")).thenReturn(Optional.of(wholesaler));

        when(batchLogisticsRepo.existsByBatch(batch)).thenReturn(false);

        batchService.transferBatch(request);

        verify(batchLogisticsRepo, times(1)).save(any(BatchLogistics.class));
    }

    @Test
    @DisplayName("Manufacturer should FAIL to transfer if batch already moved")
    void transferBatch_Manufacturer_AlreadyMoved_Fail() {
        TransferBatchRequest request = new TransferBatchRequest("batch-uuid", "user-1-uuid", "user-2-uuid");

        when(batches.findById("batch-uuid")).thenReturn(Optional.of(batch));
        when(userAccounts.findById("user-1-uuid")).thenReturn(Optional.of(testmanufacturer));
        when(userAccounts.findById("user-2-uuid")).thenReturn(Optional.of(wholesaler));

        when(batchLogisticsRepo.existsByBatch(batch)).thenReturn(true);

        assertThrows(RestrictedTransferException.class, () -> batchService.transferBatch(request));
    }

    @Test
    @DisplayName("Wholesaler to Retailer Success (with Custody)")
    void transferBatch_WholesalerToRetailer_Success() {
        // String IDs: Wholesaler (2) -> Retailer (3)
        TransferBatchRequest request = new TransferBatchRequest("batch-uuid", "user-2-uuid", "user-3-uuid");

        when(batches.findById("batch-uuid")).thenReturn(Optional.of(batch));
        when(userAccounts.findById("user-2-uuid")).thenReturn(Optional.of(wholesaler));
        when(userAccounts.findById("user-3-uuid")).thenReturn(Optional.of(retailer));

        BatchLogistics lastMove = new BatchLogistics();
        lastMove.setRecipient(wholesaler);
        when(batchLogisticsRepo.findTopByBatchOrderByCreatedDesc(batch)).thenReturn(Optional.of(lastMove));

        batchService.transferBatch(request);

        verify(batchLogisticsRepo, times(1)).save(any(BatchLogistics.class));
    }

    @Test
    @DisplayName("Wholesaler Transfer FAIL: Custody Mismatch (Double Spending)")
    void transferBatch_Wholesaler_CustodyFail() {
        TransferBatchRequest request = new TransferBatchRequest("batch-uuid", "user-2-uuid", "user-3-uuid");

        when(batches.findById("batch-uuid")).thenReturn(Optional.of(batch));
        when(userAccounts.findById("user-2-uuid")).thenReturn(Optional.of(wholesaler));
        when(userAccounts.findById("user-3-uuid")).thenReturn(Optional.of(retailer));

        BatchLogistics lastMove = new BatchLogistics();
      
        lastMove.setRecipient(retailer);

        when(batchLogisticsRepo.findTopByBatchOrderByCreatedDesc(batch)).thenReturn(Optional.of(lastMove));

        RestrictedTransferException ex = assertThrows(RestrictedTransferException.class,
                () -> batchService.transferBatch(request));

        assertTrue(ex.getMessage().contains("Custody Error"));
    }

    @Test
    @DisplayName("Retailer sells pack successfully (Ownership via Batch Custody)")
    void sellPack_Success_BatchOwnership() {
        SellPackRequest request = new SellPackRequest();
        request.setPackId("PAN-P-456");
        request.setRetailerId("user-3-uuid"); // String ID

        when(packs.findByVerificationCode("PAN-P-456")).thenReturn(Optional.of(pack));
        when(userAccounts.findById("user-3-uuid")).thenReturn(Optional.of(retailer));

        when(packLogisticsRepo.findTopByPackOrderByCreatedDesc(pack)).thenReturn(Optional.empty());

        BatchLogistics batchMove = new BatchLogistics();
        batchMove.setRecipient(retailer);
        when(batchLogisticsRepo.findTopByBatchOrderByCreatedDesc(batch)).thenReturn(Optional.of(batchMove));

        SellPackResponse response = batchService.sellPack(request);

        assertTrue(pack.isSold());
        assertEquals("Pack marked as SOLD to consumer.", response.getMessage());
        verify(packs).save(pack);
    }

    @Test
    @DisplayName("Sell Pack FAIL: Item already sold")
    void sellPack_Fail_AlreadySold() {
        SellPackRequest request = new SellPackRequest();
        request.setPackId("PAN-P-456");
        request.setRetailerId("user-3-uuid");

        pack.setSold(true);

        when(packs.findByVerificationCode("PAN-P-456")).thenReturn(Optional.of(pack));
        when(userAccounts.findById("user-3-uuid")).thenReturn(Optional.of(retailer));

        assertThrows(RestrictedTransferException.class, () -> batchService.sellPack(request));
    }

    @Test
    @DisplayName("Sell Pack FAIL: Retailer does not own the batch or pack")
    void sellPack_Fail_NoCustody() {
        SellPackRequest request = new SellPackRequest();
        request.setPackId("PAN-P-456");
        request.setRetailerId("user-3-uuid");

        when(packs.findByVerificationCode("PAN-P-456")).thenReturn(Optional.of(pack));
        when(userAccounts.findById("user-3-uuid")).thenReturn(Optional.of(retailer));

        when(packLogisticsRepo.findTopByPackOrderByCreatedDesc(pack)).thenReturn(Optional.empty());

        BatchLogistics batchMove = new BatchLogistics();
        batchMove.setRecipient(wholesaler);
        when(batchLogisticsRepo.findTopByBatchOrderByCreatedDesc(batch)).thenReturn(Optional.of(batchMove));

        assertThrows(RestrictedTransferException.class, () -> batchService.sellPack(request));
    }
}