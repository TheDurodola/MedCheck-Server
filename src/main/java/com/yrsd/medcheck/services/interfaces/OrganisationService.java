package com.yrsd.medcheck.services.interfaces;

import com.yrsd.medcheck.dtos.requests.CreateOrganisationRequest;
import com.yrsd.medcheck.dtos.responses.CreateOrganisationResponse;
import lombok.NonNull;


public interface OrganisationService {
    CreateOrganisationResponse createOrganisation(@NonNull CreateOrganisationRequest request);
}
