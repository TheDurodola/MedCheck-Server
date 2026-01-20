package com.yrsd.medcheck.services;

import com.yrsd.medcheck.data.repositories.Batches;
import com.yrsd.medcheck.data.repositories.Packs;
import com.yrsd.medcheck.data.repositories.Tablets;
import com.yrsd.medcheck.dtos.requests.CreateNewBatchRequest;
import com.yrsd.medcheck.dtos.responses.CreateNewBatchResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class BatchServiceImplTest {

    @Mock
    private Tablets tablets;

    @Mock
    private Packs  packs;

    @Mock
    private Batches batches;

    @InjectMocks
    private BatchServiceImpl batchService;


    @Test
    public  void createNewBatch() {
        CreateNewBatchRequest request = new CreateNewBatchRequest();

    }

}