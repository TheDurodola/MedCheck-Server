package com.yrsd.medcheck.dtos.responses;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CreateOrganisationResponse {
    private String organisationName;
    private String organisationType;
    private String organisationCode;
    private String createdTime;
}
