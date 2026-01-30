package com.yrsd.medcheck.services;

import com.yrsd.medcheck.data.models.Organisation;
import com.yrsd.medcheck.data.models.enums.OrganisationType;
import com.yrsd.medcheck.data.repositories.Organisations;
import com.yrsd.medcheck.dtos.requests.CreateOrganisationRequest;
import com.yrsd.medcheck.dtos.responses.CreateOrganisationResponse;
import com.yrsd.medcheck.services.interfaces.OrganisationService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.yrsd.medcheck.utils.CodeGenerator.generateCode;
import static com.yrsd.medcheck.utils.Mutator.*;

@Service
@RequiredArgsConstructor
public class OrganisationServiceImpl implements OrganisationService {

    private final Organisations organisations;

    @Override
    public CreateOrganisationResponse createOrganisation(@NonNull CreateOrganisationRequest request) {
        Organisation organisation = new Organisation();

        organisation.setName(toTitleCase(request.getName()));
        organisation.setOrganisationType(OrganisationType.valueOf(request.getOrganizationType().toUpperCase()));
        organisation.setOrganizationCode(generateCode(removeWhitespace(request.getName().toUpperCase())));
        Organisation saved = organisations.save(organisation);

        return getCreateOrganisationResponse(saved);
    }

    private static  CreateOrganisationResponse getCreateOrganisationResponse(Organisation saved) {
        CreateOrganisationResponse response = new CreateOrganisationResponse();
        response.setOrganisationName(saved.getName());
        response.setOrganisationType(saved.getOrganisationType().toString());
        response.setCreatedTime(formatInstant(saved.getCreatedDate()));
        return response;
    }
}