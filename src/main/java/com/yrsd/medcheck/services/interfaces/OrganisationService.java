package com.yrsd.medcheck.services.interfaces;

import com.yrsd.medcheck.data.models.enums.OrganisationType;
import com.yrsd.medcheck.dtos.requests.CreateOrganisationRequest;
import com.yrsd.medcheck.dtos.responses.AllOrganisationsResponse;
import com.yrsd.medcheck.dtos.responses.CreateOrganisationResponse;
import com.yrsd.medcheck.dtos.responses.GetAllOrganisationByTypeResponse;

import lombok.NonNull;

public interface OrganisationService {
    CreateOrganisationResponse createOrganisation(@NonNull CreateOrganisationRequest request);

    AllOrganisationsResponse getAllOrganisations();
    GetAllOrganisationByTypeResponse getAllOrganisationsByType(OrganisationType type);

    default AllOrganisationsResponse getAllOrganisation() {
        return getAllOrganisations();
    }
}
