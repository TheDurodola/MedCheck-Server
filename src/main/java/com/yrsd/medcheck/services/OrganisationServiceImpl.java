package com.yrsd.medcheck.services;

import com.yrsd.medcheck.data.models.Organisation;
import com.yrsd.medcheck.data.models.enums.OrganisationType;
import com.yrsd.medcheck.data.repositories.Organisations;
import com.yrsd.medcheck.dtos.requests.CreateOrganisationRequest;
import com.yrsd.medcheck.dtos.responses.CreateOrganisationResponse;
import com.yrsd.medcheck.services.interfaces.OrganisationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import static com.yrsd.medcheck.utils.CodeGenerator.generateCode;
import static com.yrsd.medcheck.utils.CodeGenerator.generateNumCode;
import static com.yrsd.medcheck.utils.Mutator.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrganisationServiceImpl implements OrganisationService {

    private final Organisations organisations;

    @Override
    public CreateOrganisationResponse createOrganisation(@NonNull CreateOrganisationRequest request) {
        log.info("Creating organisation");
        log.info("Organisation Type {}",request.getOrganizationType());
        log.info("Organisation Name {}",request.getName());
        Organisation organisation = new Organisation();
        organisation.setName(toTitleCase(request.getName()));
        organisation.setOrganisationType(OrganisationType.valueOf(request.getOrganizationType().toUpperCase()));
        organisation.setOrganizationCode(generateCode(removeWhitespace(request.getName().toUpperCase())) + generateNumCode());
        Organisation saved = organisations.save(organisation);

        return getCreateOrganisationResponse(saved);
    }

    private static  CreateOrganisationResponse getCreateOrganisationResponse(Organisation saved) {
        CreateOrganisationResponse response = new CreateOrganisationResponse();
        response.setOrganisationName(saved.getName());
        response.setOrganisationCode(saved.getOrganizationCode());
        response.setOrganisationType(saved.getOrganisationType().toString());
        response.setCreatedTime(formatInstant(saved.getCreatedDate()));
        return response;
    }
}