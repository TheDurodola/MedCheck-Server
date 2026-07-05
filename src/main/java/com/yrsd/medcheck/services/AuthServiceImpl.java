package com.yrsd.medcheck.services;

import com.yrsd.medcheck.data.models.Organisation;
import com.yrsd.medcheck.data.models.UserAccount;
import com.yrsd.medcheck.data.models.enums.Role;
import com.yrsd.medcheck.data.repositories.Organisations;
import com.yrsd.medcheck.data.repositories.UserAccounts;
import com.yrsd.medcheck.dtos.requests.RegisterUserRequest;
import com.yrsd.medcheck.dtos.requests.UploadProfilePictureRequest;
import com.yrsd.medcheck.dtos.responses.CloudServiceResponse;
import com.yrsd.medcheck.dtos.responses.RegisterUserResponse;
import com.yrsd.medcheck.dtos.responses.UploadProfilePictureResponse;
import com.yrsd.medcheck.events.UserRegisteredEvent;
import com.yrsd.medcheck.events.publishers.AuthEventPublisher;
import com.yrsd.medcheck.exceptions.*;
import com.yrsd.medcheck.proxy.cloud.CloudClient;
import com.yrsd.medcheck.services.interfaces.AuthService;
import com.yrsd.medcheck.utils.Validator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.modelmapper.ModelMapper;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.yrsd.medcheck.utils.Mutator.mutate;
import static com.yrsd.medcheck.utils.Mutator.standardizePhoneNumber;
import static com.yrsd.medcheck.utils.Validator.validate;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final PasswordEncoder passwordEncoder;
    private final UserAccounts userAccounts;
    private final ModelMapper modelMapper;
    private final Organisations organisations;
    private final CloudClient cloudClient;
    private final AuthEventPublisher eventPublisher;


    @Override
    @Transactional
    public RegisterUserResponse registerUser(RegisterUserRequest request) {
        validate(request);
        log.info("user {} fields validated", request.getUsername());
        mutate(request);
        log.info("user {} fields mutated", request.getUsername());
        validateUniqueness(request);


        if (!Validator.isValid(request.getPhoneNumber())) {
            throw new InvalidPhoneNumberException("This is not a Nigerian phone number");
        }

        request.setPhoneNumber(standardizePhoneNumber(request.getPhoneNumber()));

        log.info("testing user role {}", request.getRole());
        UserAccount userAccount = modelMapper.map(request, UserAccount.class);
        userAccount.setRole(Role.valueOf(request.getRole().toUpperCase()));

        userAccount.setPassword(passwordEncoder.encode(request.getPassword()));
        mutate(userAccount);


        if (!Role.CONSUMER.name().equalsIgnoreCase(request.getRole()) && !Role.ADMINISTRATOR.name()
                .equalsIgnoreCase(request.getRole())
                && !Role.INVESTIGATOR.name().equalsIgnoreCase(request.getRole())) {

            if (request.getOrganisationId() == null | request.getOrganisationId() == null) {
                throw new InvalidOrganisationCodeException("The organization fields ae required for retailers, " +
                        "wholesalers, and manufacturers.");
            }

            Organisation organisation = organisations.findById(request.getOrganisationId()).orElseThrow(() ->
                    new OrganizationDoesntExistException("This organisation doesn't exist"));

            if (!request.getOrganisationCode().equals(organisation.getOrganizationCode())) {
                throw new InvalidOrganisationCodeException("Your organisation code is invalid. " +
                        "Contact the appropriate personnel");
            }
            organisation.addUserAccount(userAccount);
        }

        UserAccount savedUserAccount = userAccounts.save(userAccount);
        UserRegisteredEvent registeredEvent = new UserRegisteredEvent(
                savedUserAccount.getFirstName(), savedUserAccount.getLastName(), savedUserAccount.getEmail());
        eventPublisher.WelcomeEmailEvent(registeredEvent);
        log.info("user {} added to database", request.getUsername());
        return modelMapper.map(savedUserAccount, RegisterUserResponse.class);
    }



    public UploadProfilePictureResponse uploadProfilePicture(UploadProfilePictureRequest request){
        CloudServiceResponse cloudServiceResponse = cloudClient.uploadProfilePicture(request);
        //TODO: Work on Profile Picture
//        userAccount.setProfilePictureUrl(cloudServiceResponse.getImageUrl());
        return null;
    }

    private void validateUniqueness(@NonNull RegisterUserRequest request) {
        if (userAccounts.existsByUsername(request.getUsername())) {
            throw new UsernameAlreadyExistException(request.getUsername() + " already exists");
        }
        if (userAccounts.existsByEmail(request.getEmail())) {
            throw new EmailAlreadyExistException(request.getEmail() + " already exists");
        }
    }


}
