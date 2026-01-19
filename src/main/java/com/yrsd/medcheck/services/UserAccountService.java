package com.yrsd.medcheck.services;

import com.yrsd.medcheck.security.dtos.responses.UserAccountResponse;

public interface UserAccountService {
    UserAccountResponse getUserAccountBy(String username);
}
