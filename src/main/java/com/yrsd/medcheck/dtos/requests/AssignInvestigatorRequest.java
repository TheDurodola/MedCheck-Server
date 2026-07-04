package com.yrsd.medcheck.dtos.requests;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AssignInvestigatorRequest {
    private String userId;
    private String unitId;
}
