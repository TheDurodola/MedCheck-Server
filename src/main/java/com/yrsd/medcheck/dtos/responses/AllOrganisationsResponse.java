package com.yrsd.medcheck.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AllOrganisationsResponse {
    private List<OrganisationResponse> organisations = new ArrayList<>();

    public List<OrganisationResponse> getOrganizations() {
        return organisations;
    }

    public void setOrganizations(List<OrganisationResponse> organizations) {
        this.organisations = organizations;
    }
}
