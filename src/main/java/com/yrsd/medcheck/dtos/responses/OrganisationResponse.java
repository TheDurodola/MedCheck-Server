package com.yrsd.medcheck.dtos.responses;

import com.yrsd.medcheck.data.models.enums.OrganisationType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrganisationResponse {
    private String id;
    private OrganisationType organisationType;

    public OrganisationResponse(String id, String organisationType) {
        this.id = id;
        this.organisationType = organisationType != null ? OrganisationType.valueOf(organisationType.toUpperCase()) : null;
    }

    public OrganisationType getOrganizationType() {
        return organisationType;
    }

    public void setOrganizationType(OrganisationType organizationType) {
        this.organisationType = organizationType;
    }
}
