package com.yrsd.medcheck.dtos.responses;

import com.yrsd.medcheck.dtos.OrganisationData;


import java.util.List;

public record GetAllOrganisationByTypeResponse(List<OrganisationData> organisation) {
}
