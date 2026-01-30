//package com.yrsd.medcheck.services;
//
//import com.yrsd.medcheck.data.models.*;
//import com.yrsd.medcheck.data.models.enums.Role;
//import com.yrsd.medcheck.data.repositories.*;
//import com.yrsd.medcheck.dtos.requests.*;
//import com.yrsd.medcheck.dtos.responses.*;
//import com.yrsd.medcheck.exceptions.*;
//import com.yrsd.medcheck.utils.CodeGenerator;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.Spy;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import java.math.BigInteger;
//import java.time.Instant;
//import java.time.LocalDate;
//import java.util.ArrayList;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//class BatchServiceImplTest {
//
//    @Spy
//    CodeGenerator codeGenerator;
//
//    @Mock UserAccounts userAccounts;
//    @Mock Drugs drugs;
//    @Mock Packs packs;
//    @Mock BatchLogisticsRepo batchLogisticsRepo;
//    @Mock PackLogisticsRepo packLogisticsRepo;
//    @Mock Sachets sachets;
//    @Mock Batches batches;
//
//    @InjectMocks
//    private BatchServiceImpl batchService;
//
//
//    private UserAccount manufacturer;
//    private UserAccount wholesaler;
//    private UserAccount retailer;
//    private Drug drug;
//    private Batch batch;
//    private Pack pack;
//    private Sachet sachet;
//
//    @BeforeEach
//    void setUp() {
//
//        manufacturer = new UserAccount();
//        manufacturer.setId("user-1-uuid");
//        manufacturer.setUsername("Pfizer-NG");
//        manufacturer.setRole(Role.MANUFACTURER);
//
//        wholesaler = new UserAccount();
//        wholesaler.setId("user-2-uuid");
//        wholesaler.setUsername("Big-Pharma-Dist");
//        wholesaler.setRole(Role.WHOLESALER);
//
//        retailer = new UserAccount();
//        retailer.setId("user-3-uuid");
//        retailer.setUsername("Olatunde-Pharmacy");
//        retailer.setRole(Role.RETAIL_EMPLOYEE);
//
//
//        drug = new Drug();
//        drug.setId("drug-100-uuid");
//        drug.setDrugCode("PAN");
//        drug.setBrandName("Panadol");
//        drug.setGenericName("Paracetamol");
//        drug.setManufacturing_company(manufacturer);
//        drug.setExpiryDurationInDays(365);
//
//
//        batch = new Batch();
//        batch.setId("batch-uuid");
//        batch.setVerificationCode("PAN-B-123");
//        batch.setVerificationCount(BigInteger.ZERO);
//        batch.setManufactureDate(LocalDate.now());
//        batch.setExpirationDate(LocalDate.now().plusDays(365));
//        batch.setCreated(Instant.now());
//        batch.setDrug(drug);
//        batch.setPacks(new ArrayList<>());
//
//
//        pack = new Pack();
//        pack.setId("pack-uuid");
//        pack.setVerificationCode("PAN-P-456");
//        pack.setVerificationCount(BigInteger.ZERO);
//        pack.setBatch(batch);
//        pack.setDrug(drug);
//        pack.setSachets(new ArrayList<>());
//
//        batch.getPacks().add(pack);
//
//
//        sachet = new Sachet();
//        sachet.setId("sachet-uuid");
//        sachet.setVerificationCode("PAN-S-789");
//        sachet.setVerificationCount(BigInteger.ZERO);
//        sachet.setPack(pack);
//        sachet.setDrug(drug);
//
//        pack.getSachets().add(sachet);
//    }
//
//
//
//    @Test
//    void createBatch_Successful_ShouldCreateHierarchyAndReturnResponse() {
//        CreateBatchRequest request = new CreateBatchRequest();
//        request.setDrugId(drug.getId());
//        request.setManufacturing_employee_Id(manufacturer.getUsername());
//        request.setAmountOfBatches(1);
//        request.setAmountOfPacks(1);
//        request.setAmountOfSachets(1);
//
//        when(drugs.findById(drug.getId())).thenReturn(Optional.of(drug));
//        when(batches.saveAll(any())).thenAnswer(invocation -> invocation.getArgument(0));
//
//        CreateBatchResponse response = batchService.createBatch(request);
//
//        assertNotNull(response);
//        assertEquals("Batch created", response.getMessage());
//        verify(batches, times(1)).saveAll(anyList());
//    }
//
//    @Test
//    void createBatch_UnauthorizedManufacturer_ShouldThrowException() {
//        CreateBatchRequest request = new CreateBatchRequest();
//        request.setDrugId(drug.getId());
//        request.setManufacturing_employee_Id("Wrong-Manufacturer-Name");
//
//        when(drugs.findById(drug.getId())).thenReturn(Optional.of(drug));
//
//        assertThrows(UnauthorizedException.class, () -> batchService.createBatch(request));
//        verify(batches, never()).saveAll(any());
//    }
//
//
//
//    @Test
//    @DisplayName("Manufacturer should transfer to Wholesaler successfully")
//    void transferBatch_ManufacturerToWholesaler_Success() {
//        TransferBatchRequest request = new TransferBatchRequest("batch-uuid", "user-1-uuid", "user-2-uuid");
//
//        when(batches.findById("batch-uuid")).thenReturn(Optional.of(batch));
//        when(userAccounts.findById("user-1-uuid")).thenReturn(Optional.of(manufacturer));
//        when(userAccounts.findById("user-2-uuid")).thenReturn(Optional.of(wholesaler));
//
//        when(batchLogisticsRepo.existsByBatch(batch)).thenReturn(false);
//
//        batchService.transferBatch(request);
//
//        verify(batchLogisticsRepo, times(1)).save(any(BatchLogistics.class));
//    }
//
//    @Test
//    @DisplayName("Manufacturer FAIL: Batch already moved")
//    void transferBatch_Manufacturer_AlreadyMoved_Fail() {
//        TransferBatchRequest request = new TransferBatchRequest("batch-uuid", "user-1-uuid", "user-2-uuid");
//
//        when(batches.findById("batch-uuid")).thenReturn(Optional.of(batch));
//        when(userAccounts.findById("user-1-uuid")).thenReturn(Optional.of(manufacturer));
//        when(userAccounts.findById("user-2-uuid")).thenReturn(Optional.of(wholesaler));
//
//        when(batchLogisticsRepo.existsByBatch(batch)).thenReturn(true);
//
//        assertThrows(RestrictedTransferException.class, () -> batchService.transferBatch(request));
//    }
//
//    @Test
//    @DisplayName("Wholesaler to Retailer Success (Custody Verified)")
//    void transferBatch_WholesalerToRetailer_Success() {
//        TransferBatchRequest request = new TransferBatchRequest("batch-uuid", "user-2-uuid", "user-3-uuid");
//
//        when(batches.findById("batch-uuid")).thenReturn(Optional.of(batch));
//        when(userAccounts.findById("user-2-uuid")).thenReturn(Optional.of(wholesaler));
//        when(userAccounts.findById("user-3-uuid")).thenReturn(Optional.of(retailer));
//
//        BatchLogistics lastMove = new BatchLogistics();
//        lastMove.setRecipient(wholesaler);
//        when(batchLogisticsRepo.findTopByBatchOrderByCreatedDesc(batch)).thenReturn(Optional.of(lastMove));
//
//        batchService.transferBatch(request);
//
//        verify(batchLogisticsRepo, times(1)).save(any(BatchLogistics.class));
//    }
//
//    @Test
//    @DisplayName("Wholesaler FAIL: Custody Mismatch")
//    void transferBatch_Wholesaler_CustodyFail() {
//        TransferBatchRequest request = new TransferBatchRequest("batch-uuid", "user-2-uuid", "user-3-uuid");
//
//        when(batches.findById("batch-uuid")).thenReturn(Optional.of(batch));
//        when(userAccounts.findById("user-2-uuid")).thenReturn(Optional.of(wholesaler));
//        when(userAccounts.findById("user-3-uuid")).thenReturn(Optional.of(retailer));
//
//        BatchLogistics lastMove = new BatchLogistics();
//        lastMove.setRecipient(retailer); // Wholesaler doesn't have it!
//        when(batchLogisticsRepo.findTopByBatchOrderByCreatedDesc(batch)).thenReturn(Optional.of(lastMove));
//
//        RestrictedTransferException ex = assertThrows(RestrictedTransferException.class,
//                () -> batchService.transferBatch(request));
//
//        assertTrue(ex.getMessage().contains("Custody Error"));
//    }
//
//
//
//    @Test
//    @DisplayName("Sell Pack Success: Retailer owns Parent Batch")
//    void sellPack_Success() {
//        SellPackRequest request = new SellPackRequest();
//        request.setPackId(pack.getId()); // Use ID, not Verification Code
//        request.setRetailerId(retailer.getId());
//
//        when(packs.findById(pack.getId())).thenReturn(Optional.of(pack));
//        when(userAccounts.findById(retailer.getId())).thenReturn(Optional.of(retailer));
//
//
//        when(packLogisticsRepo.findTopByPackOrderByCreatedDesc(pack)).thenReturn(Optional.empty());
//
//        BatchLogistics batchMove = new BatchLogistics();
//        batchMove.setRecipient(retailer);
//        when(batchLogisticsRepo.findTopByBatchOrderByCreatedDesc(batch)).thenReturn(Optional.of(batchMove));
//
//        SellPackResponse response = batchService.sellPack(request);
//
//        assertTrue(pack.isSold());
//        assertEquals("Pack marked as SOLD to consumer.", response.getMessage());
//    }
//
//    @Test
//    @DisplayName("Sell Pack FAIL: Already Sold")
//    void sellPack_Fail_AlreadySold() {
//        SellPackRequest request = new SellPackRequest();
//        request.setPackId(pack.getId());
//        request.setRetailerId(retailer.getId());
//
//        pack.setSold(true);
//
//        when(packs.findById(pack.getId())).thenReturn(Optional.of(pack));
//        when(userAccounts.findById(retailer.getId())).thenReturn(Optional.of(retailer));
//
//        assertThrows(RestrictedTransferException.class, () -> batchService.sellPack(request));
//    }
//
//    @Test
//    @DisplayName("Sell Pack FAIL: No Custody")
//    void sellPack_Fail_NoCustody() {
//        SellPackRequest request = new SellPackRequest();
//        request.setPackId(pack.getId());
//        request.setRetailerId(retailer.getId());
//
//        when(packs.findById(pack.getId())).thenReturn(Optional.of(pack));
//        when(userAccounts.findById(retailer.getId())).thenReturn(Optional.of(retailer));
//
//        when(packLogisticsRepo.findTopByPackOrderByCreatedDesc(pack)).thenReturn(Optional.empty());
//
//
//        BatchLogistics batchMove = new BatchLogistics();
//        batchMove.setRecipient(wholesaler);
//        when(batchLogisticsRepo.findTopByBatchOrderByCreatedDesc(batch)).thenReturn(Optional.of(batchMove));
//
//        assertThrows(RestrictedTransferException.class, () -> batchService.sellPack(request));
//    }
//
//    @Test
//    void verifyBatch_Successful() {
//        VerifyBatchRequest request = new VerifyBatchRequest();
//        request.setBatchVerificationCode(batch.getVerificationCode());
//
//        when(batches.findByVerificationCode(batch.getVerificationCode()))
//                .thenReturn(Optional.of(batch));
//
//
//        when(batchLogisticsRepo.findByBatchIdOrderByCreatedAsc(batch.getId()))
//                .thenReturn(Optional.of(new ArrayList<>()));
//
//        VerifyBatchResponse response = batchService.verifyBatch(request);
//
//        verify(batches, times(1)).save(batch);
//        assertEquals(BigInteger.ONE, batch.getVerificationCount());
//        assertNotNull(response.getBatch());
//    }
//
//    @Test
//    void verifyPack_Successful() {
//        VerifyPackRequest request = new VerifyPackRequest();
//        request.setPackVerificationCode(pack.getVerificationCode());
//
//        when(packs.findByVerificationCode(pack.getVerificationCode()))
//                .thenReturn(Optional.of(pack));
//
//        VerifyPackResponse response = batchService.verifyPack(request);
//
//        verify(packs, times(1)).save(pack);
//        assertEquals(BigInteger.ONE, pack.getVerificationCount());
//        assertEquals(batch.getVerificationCode(), response.getBatch().get("batchCode"));
//    }
//
//    @Test
//    void verifySachet_Successful() {
//        VerifySachetRequest request = new VerifySachetRequest();
//        request.setSachetVerificationCode(sachet.getVerificationCode());
//
//        when(sachets.findByVerificationCode(sachet.getVerificationCode()))
//                .thenReturn(Optional.of(sachet));
//
//        VerifySachetResponse response = batchService.verifySachet(request);
//
//        verify(sachets, times(1)).save(sachet);
//        assertEquals(BigInteger.ONE, sachet.getVerificationCount());
//        assertEquals(pack.getVerificationCode(), response.getPack().get("packCode"));
//    }
//}