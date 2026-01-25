package com.yrsd.medcheck.services.interfaces;

import com.yrsd.medcheck.security.dtos.responses.UserAccountResponse;

public interface UserAccountService {
    UserAccountResponse getUserAccountBy(String username);
}
