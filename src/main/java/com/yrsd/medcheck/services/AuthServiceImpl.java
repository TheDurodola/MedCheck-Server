package com.yrsd.medcheck.services;

import com.yrsd.medcheck.data.models.UserAccount;
import com.yrsd.medcheck.data.repositories.UserAccounts;
import com.yrsd.medcheck.dtos.requests.RegisterUserRequest;
import com.yrsd.medcheck.dtos.responses.CloudServiceResponse;
import com.yrsd.medcheck.dtos.responses.RegisterUserResponse;
import com.yrsd.medcheck.exceptions.EmailAlreadyExistException;
import com.yrsd.medcheck.exceptions.UsernameAlreadyExistException;
import com.yrsd.medcheck.proxy.cloud.CloudService;
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
    private final CloudService cloudService;

    public RegisterUserResponse registerUser(RegisterUserRequest request) {
        validate(request);
        log.info("user {} fields validated", request.getUsername());
        mutate(request);
        log.info("user {} fields mutated", request.getUsername());
        validateUniqueness(request);

        CloudServiceResponse cloudServiceResponse = cloudService.uploadProfilePicture(request);

        UserAccount userAccount = modelMapper.map(request, UserAccount.class);
        log.info("testing user role {}", userAccount.getRole().toString());

        userAccount.setPassword(passwordEncoder.encode(request.getPassword()));
        userAccount.setProfilePictureUrl(cloudServiceResponse.getImageUrl());
        UserAccount savedUserAccount = userAccounts.save(userAccount);
        log.info("user {} added to database", request.getUsername());
        return modelMapper.map(savedUserAccount, RegisterUserResponse.class);
    }

    private void validateUniqueness(RegisterUserRequest request) {
        if (userAccounts.existsByUsername(request.getUsername())) {
            throw new UsernameAlreadyExistException(request.getUsername() + " already exists");
        }
        if (userAccounts.existsByEmail(request.getEmail())) {
            throw new EmailAlreadyExistException(request.getEmail() + " already exists");
        }
    }




}
