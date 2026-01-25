package com.yrsd.medcheck.services;

import com.yrsd.medcheck.data.repositories.UserAccounts;
import com.yrsd.medcheck.exceptions.AccountNotFoundException;
import com.yrsd.medcheck.security.dtos.responses.UserAccountResponse;
import com.yrsd.medcheck.services.interfaces.UserAccountService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserAccountServiceImpl implements UserAccountService {

    private final UserAccounts userAccounts;
    private final ModelMapper modelMapper;

    @Override
    public UserAccountResponse getUserAccountBy(String username) {
        if (userAccounts.findByUsername(username).isPresent()) {
            return modelMapper.map(userAccounts.findByUsername(username).get(), UserAccountResponse.class);
        }
        throw new AccountNotFoundException("User not found");
    }
}
