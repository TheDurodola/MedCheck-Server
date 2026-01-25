package com.yrsd.medcheck.utils;

import com.yrsd.medcheck.data.models.enums.Gender;
import com.yrsd.medcheck.dtos.requests.RegisterUserRequest;
import com.yrsd.medcheck.exceptions.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.tika.Tika;

import java.io.IOException;
import java.time.LocalDate;
import java.time.Period;
import java.util.Arrays;
import java.util.List;

@Slf4j

public class Validator {

    private static final Tika tika = new Tika();
    private static final List<String> ALLOWED_MIME_TYPES = Arrays.asList(
            "image/jpeg",
            "image/png"
    );


    public static void validate(RegisterUserRequest request) {
        validateNames(request);
        validateEmail(request);
        validatePassword(request);
        validateGender(request);
        validateUsername(request);
        validateDateOfBirth(request);
        validateNationalIdentityNUmber(request);
    }

    private static void validateNames(RegisterUserRequest request) {
        validateName(request.getFirstName(), "Firstname");
        validateName(request.getLastName(), "Lastname");
        validateName(request.getMiddleName(), "MiddleName");
    }

    private static void validateName(String request, String message) {
        if (request == null || request.isBlank()) {
            throw new InvalidNameException(message + " field cannot be null or empty");
        }
        if (request.chars().anyMatch(Character::isDigit)) {
            throw new InvalidNameException(message + " cannot contain digits");
        };
    }

    private static void validateUsername( RegisterUserRequest request) {
        if (request.getUsername() == null || request.getUsername().isBlank()){
            throw new InvalidUsernameException("Username field cannot be null or empty");
        }
        if (request.getUsername().length() < 4){
            throw new InvalidUsernameException("Username length should be at least 4");
        }

        if (!request.getUsername().matches("^[a-zA-Z][a-zA-Z0-9_]{2,15}$")) {
            throw new InvalidUsernameException("Invalid Username");
        }

    }

    private static void validateGender(RegisterUserRequest request) {
        if (request.getGender() == null || request.getGender().isBlank()){
            throw new InvalidGenderException("Gender field cannot be null or empty");
        }

        if (Arrays.stream(Gender.values())
                .anyMatch(g -> Arrays.toString(Gender.values()).equalsIgnoreCase(request.getGender()))){
            throw new InvalidGenderException("Invalid Gender");
        };
    }

    private static void validateEmail( RegisterUserRequest request) {
        if (request.getEmail() == null || request.getEmail().isBlank()){
            throw new InvalidEmailException("Email Address field cannot be null or empty");
        }

        if (!request.getEmail().matches("^[\\w._%+-]+@[\\w.-]+\\.[a-zA-Z]{2,}$")){
            throw new InvalidEmailException("Invalid Email Address format");
        };

    }

    private static void validatePassword(RegisterUserRequest request) {
        if (request.getPassword() == null || request.getPassword().isBlank()){
            throw new InvalidPasswordException("Password field cannot be null or empty");
        }
        if (request.getPassword().length() < 6){
            throw new InvalidPasswordException("Password length cannot be less than 6 characters");
        }

        if (request.getPassword().chars()
                .noneMatch(Character::isUpperCase)){
            throw new InvalidPasswordException("Password must contain at least one uppercase character");
        }
        if (request.getPassword().chars()
                .noneMatch(Character::isDigit)){
            throw new InvalidPasswordException("Password must contain at least one digit character");
        }
    }

    private static void validateNationalIdentityNUmber(RegisterUserRequest request){
        if (!(request.getNationalIdentityNumber().length() == 12)){
            throw new InvalidNationalIdentityNumberException("NIN must be 12 digits long");
        }
    }
    private static void validateDateOfBirth( RegisterUserRequest request) {
        if (request.getDateOfBirth() == null){
            throw new InvalidDateOfBirthException("The Date of Birth field cannot be null");
        }
        Period period = Period.between(request.getDateOfBirth(), LocalDate.now());
        if (period.getYears() <= 12){
            throw new InvalidDateOfBirthException("Must be older than 12 years old");
        }
        if (period.getYears() >= 100){
            throw new InvalidDateOfBirthException("Must be younger than 100 years old");
        }
    }

    //    private static void validateProfilePicture( RegisterUserRequest request)  {
//        if (request.getProfilePicture() == null || request.getProfilePicture().isEmpty()){
//            throw new InvalidProfilePictureException("The Profile Picture field cannot be null");
//        }
//
//        try {
//            String fileType = tika.detect(request.getProfilePicture().getInputStream());
//
//            if (!ALLOWED_MIME_TYPES.contains(fileType)) {
//                log.warn("User {} attempted to upload a invalid file type: {}", request.getEmail(), fileType);
//                throw new InvalidProfilePictureException("Invalid file type: " + fileType);
//            }
//
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }
}
