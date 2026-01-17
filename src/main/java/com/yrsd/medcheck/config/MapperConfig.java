package com.yrsd.medcheck.config;

import com.yrsd.medcheck.data.models.UserAccount;
import com.yrsd.medcheck.dtos.requests.RegisterUserRequest;
import com.yrsd.medcheck.dtos.responses.RegisterUserResponse;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MapperConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        configureForUserAccountToRegisterUserResponse(modelMapper);
        configureForRegisterUserRequestToUserAccount(modelMapper);


        configureMatchingStrategy(modelMapper);

        return modelMapper;
    }

    public static void configureMatchingStrategy(ModelMapper modelMapper) {
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
    }

    public static void configureForRegisterUserRequestToUserAccount(ModelMapper modelMapper) {
        modelMapper.typeMap(RegisterUserRequest.class, UserAccount.class)
                .addMapping(RegisterUserRequest::getPassword, UserAccount::setPassword);
    }

    public static void configureForUserAccountToRegisterUserResponse(ModelMapper modelMapper) {
        modelMapper.typeMap(UserAccount.class, RegisterUserResponse.class)
                .addMapping(UserAccount::getFirstName, RegisterUserResponse::setFirstName);
    }
}
