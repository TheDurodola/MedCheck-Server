package com.yrsd.medcheck.services;

import com.yrsd.medcheck.data.models.UserAccount;
import com.yrsd.medcheck.data.repositories.UserAccounts;
import com.yrsd.medcheck.dtos.requests.RegisterUserRequest;
import com.yrsd.medcheck.dtos.responses.RegisterUserResponse;
import com.yrsd.medcheck.exceptions.EmailAlreadyExistException;
import com.yrsd.medcheck.exceptions.UsernameAlreadyExistException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static com.yrsd.medcheck.utils.Mutator.mutate;
import static com.yrsd.medcheck.utils.Validator.validate;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final PasswordEncoder passwordEncoder;
    private final UserAccounts userAccounts;
    private final ModelMapper modelMapper;

    public RegisterUserResponse registerUser(RegisterUserRequest request) {
        validate(request);
        mutate(request);
        validateUniqueness(request);
        UserAccount userAccount = modelMapper.map(request, UserAccount.class);

        log.error(userAccount.getPassword());
        userAccount.setPassword(passwordEncoder.encode(request.getPassword()));
        log.error(userAccount.getPassword());
        UserAccount savedUserAccount = userAccounts.save(userAccount);
        return modelMapper.map(savedUserAccount, RegisterUserResponse.class);
    }

    private void validateUniqueness(RegisterUserRequest request) {
        if (userAccounts.doesUsernameExist(request.getUsername())) {
            throw new UsernameAlreadyExistException(request.getUsername() + " already exists");
        }
        if (userAccounts.doesEmailExist(request.getEmail())) {
            throw new EmailAlreadyExistException(request.getEmail() + " already exists");
        }
    }




}
