package com.yrsd.medcheck.services.interfaces;

import com.yrsd.medcheck.dtos.requests.CreateDrugRequest;
import com.yrsd.medcheck.dtos.responses.CreateDrugResponse;

public interface DrugService {
    CreateDrugResponse createDrug(CreateDrugRequest request, String currentUser);
}
