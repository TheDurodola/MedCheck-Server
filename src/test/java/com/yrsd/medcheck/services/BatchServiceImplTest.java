package com.yrsd.medcheck.services;

import com.yrsd.medcheck.data.models.*;
import com.yrsd.medcheck.data.repositories.*;
import com.yrsd.medcheck.dtos.requests.CreateBatchRequest;
import com.yrsd.medcheck.utils.CodeGenerator;
import org.jspecify.annotations.NonNull;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Duration;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BatchServiceImplTest {


    @Spy
    CodeGenerator codeGenerator;

    Drug drug;

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


    @Test
    void thatABatchOfTwentyPacksAndTwoSachetsIsSaved() {
        Drug drug = getDrug();
        when(drugs.findById("1")).thenReturn(Optional.of(drug));
        when(batches.save(any())).thenReturn(new Batch());
        CreateBatchRequest createBatchRequest = new CreateBatchRequest();
        createBatchRequest.setAmountOfBatches(20);
        createBatchRequest.setAmountOfPacks(100);
        createBatchRequest.setAmountOfSachets(2);
        createBatchRequest.setDrugId("1");
        ArgumentCaptor<Batch> batchCaptor = ArgumentCaptor.forClass(Batch.class);
        batchService.createBatch(createBatchRequest);
        verify(batches, times(20)).save(batchCaptor.capture());

        assertThat(batchCaptor.getAllValues().size()).isEqualTo(20);
        assertThat(batchCaptor.getAllValues().getFirst().getPacks()
                .size())
                .isEqualTo(100);

        assertThat(batchCaptor.getAllValues().getFirst().getPacks()
                .getFirst().getSachets().size())
                .isEqualTo(2);

        assertThat(batchCaptor.getAllValues().getFirst().getPacks()
                .getFirst().getSachets().getFirst().getDrug().getNafdacRegistrationNumber())
                .isEqualTo("12345");

        assertThat(batchCaptor.getAllValues().getFirst().getPacks()
                .getFirst().getDrug().getNafdacRegistrationNumber())
                .isEqualTo("12345");


    }

    @Test
    void thatThatExpirationDateIsAutomaticallyGenerated()
    {
        Drug drug = getDrug();
        when(drugs.findById(any(String.class))).thenReturn(Optional.of(drug));
        when(batches.save(any())).thenReturn(new Batch());
        CreateBatchRequest createBatchRequest = new CreateBatchRequest();
        createBatchRequest.setAmountOfBatches(20);
        createBatchRequest.setAmountOfPacks(100);
        createBatchRequest.setAmountOfSachets(2);
        createBatchRequest.setDrugId("1");
        ArgumentCaptor<Batch> batchCaptor = ArgumentCaptor.forClass(Batch.class);
        batchService.createBatch(createBatchRequest);
        verify(batches, times(20)).save(batchCaptor.capture());

        assertThat(batchCaptor.getAllValues().getFirst().getExpirationDate()).isEqualTo(LocalDate.now().plusYears(1));
    }

    @Test
    void thatCreateBatchHaveDifferentVerificationNumber(){
        Drug drug = getDrug();
        Batch newBatch = new Batch();
        newBatch.setId(UUID.randomUUID().toString());
        when(batches.save(any())).thenReturn(newBatch);
        when(drugs.findById(any(String.class))).thenReturn(Optional.of(drug));
        CreateBatchRequest createBatchRequest = new CreateBatchRequest();
        createBatchRequest.setAmountOfBatches(20);
        createBatchRequest.setAmountOfPacks(100);
        createBatchRequest.setAmountOfSachets(2);
        createBatchRequest.setDrugId("1");
        ArgumentCaptor<Batch> batchCaptor = ArgumentCaptor.forClass(Batch.class);
        batchService.createBatch(createBatchRequest);
        verify(batches, times(20)).save(batchCaptor.capture());
        assertThat(batchCaptor.getAllValues().getFirst().getVerificationCode()).isNotEqualTo(batchCaptor.getAllValues().getLast().getVerificationCode());
    }








    private static @NonNull Drug getDrug() {
        Drug drug = new Drug();
        drug.setBrandName("Paracetamol");
        drug.setGenericName("Acetaminophen");
        drug.setExpiryDurationInDays(365);
        drug.setDescription("common over-the-counter medicine for reducing fever and relieving mild to moderate pain " +
                "from headaches, muscle aches, toothaches, and colds, acting as an analgesic (pain reliever) and " +
                "antipyretic (fever reducer). Sold under names like Tylenol, it's available in many forms, but taking " +
                "too much can cause severe liver damage, so it's crucial to follow dosing instructions and check labels " +
                "for its presence in other medications like cold and flu remedies.");
        drug.setNafdacRegistrationNumber("12345");
        drug.setDrugCode("PAR");
        return drug;
    }


}