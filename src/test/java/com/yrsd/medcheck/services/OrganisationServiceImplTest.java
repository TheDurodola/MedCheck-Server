package com.yrsd.medcheck.services;

import com.yrsd.medcheck.data.models.Organisation;
import com.yrsd.medcheck.data.models.enums.OrganisationType;
import com.yrsd.medcheck.data.repositories.Organisations;
import com.yrsd.medcheck.dtos.requests.CreateOrganisationRequest;
import com.yrsd.medcheck.dtos.responses.AllOrganisationsResponse;
import com.yrsd.medcheck.dtos.responses.CreateOrganisationResponse;
import com.yrsd.medcheck.dtos.responses.OrganisationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrganisationServiceImplTest {

    @Mock
    private Organisations organisations;

    @InjectMocks
    private OrganisationServiceImpl organisationService;

    private Organisation org1;
    private Organisation org2;

    @BeforeEach
    void setUp() {
        org1 = new Organisation();
        org1.setId("org-id-1");
        org1.setName("PharmaCorp Nigeria");
        org1.setOrganizationCode("PHARMACORP123");
        org1.setOrganisationType(OrganisationType.MANUFACTURE);
        org1.setCreatedDate(Instant.now());

        org2 = new Organisation();
        org2.setId("org-id-2");
        org2.setName("MediDistributors Ltd");
        org2.setOrganizationCode("MEDIDIST456");
        org2.setOrganisationType(OrganisationType.WHOLESALE);
        org2.setCreatedDate(Instant.now());
    }

    @Test
    @DisplayName("getAllOrganisations returns list of only organisation ID and organisationType")
    void getAllOrganisations_ReturnsOnlyIdAndOrganisationType() {
        when(organisations.findAll()).thenReturn(List.of(org1, org2));

        AllOrganisationsResponse response = organisationService.getAllOrganisations();

        assertNotNull(response);
        assertNotNull(response.getOrganisations());
        assertEquals(2, response.getOrganisations().size());

        OrganisationResponse first = response.getOrganisations().get(0);
        assertEquals("org-id-1", first.getId());
        assertEquals(OrganisationType.MANUFACTURE, first.getOrganisationType());

        OrganisationResponse second = response.getOrganisations().get(1);
        assertEquals("org-id-2", second.getId());
        assertEquals(OrganisationType.WHOLESALE, second.getOrganisationType());

        verify(organisations, times(1)).findAll();
    }

    @Test
    @DisplayName("getAllOrganisations returns empty list when no organisations exist")
    void getAllOrganisations_EmptyList() {
        when(organisations.findAll()).thenReturn(Collections.emptyList());

        AllOrganisationsResponse response = organisationService.getAllOrganisations();

        assertNotNull(response);
        assertNotNull(response.getOrganisations());
        assertTrue(response.getOrganisations().isEmpty());

        verify(organisations, times(1)).findAll();
    }

    @Test
    @DisplayName("getAllOrganisation alias returns identical result to getAllOrganisations")
    void getAllOrganisation_AliasMatches() {
        when(organisations.findAll()).thenReturn(List.of(org1));

        AllOrganisationsResponse response = organisationService.getAllOrganisation();

        assertNotNull(response);
        assertEquals(1, response.getOrganisations().size());
        assertEquals("org-id-1", response.getOrganisations().get(0).getId());
        assertEquals(OrganisationType.MANUFACTURE, response.getOrganisations().get(0).getOrganisationType());
    }

    @Test
    @DisplayName("createOrganisation successfully saves and returns organisation details")
    void createOrganisation_Successful() {
        CreateOrganisationRequest request = new CreateOrganisationRequest();
        request.setName("PharmaCorp Nigeria");
        request.setOrganizationType("MANUFACTURE");

        when(organisations.save(any(Organisation.class))).thenAnswer(invocation -> {
            Organisation saved = invocation.getArgument(0);
            saved.setId("org-id-1");
            saved.setCreatedDate(Instant.now());
            return saved;
        });

        CreateOrganisationResponse response = organisationService.createOrganisation(request);

        assertNotNull(response);
        assertEquals("Pharmacorp Nigeria", response.getOrganisationName());
        assertEquals("MANUFACTURE", response.getOrganisationType());
        assertNotNull(response.getOrganisationCode());
        verify(organisations, times(1)).save(any(Organisation.class));
    }
}
