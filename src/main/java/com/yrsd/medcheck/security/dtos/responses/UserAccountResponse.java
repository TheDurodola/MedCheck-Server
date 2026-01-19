package com.yrsd.medcheck.security.dtos.responses;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.yrsd.medcheck.data.models.enums.AccountStatus;
import com.yrsd.medcheck.data.models.enums.Role;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserAccountResponse {
    private String username;
    @JsonIgnore
    private String password;
    private Role role;
    private AccountStatus  accountStatus;
}
