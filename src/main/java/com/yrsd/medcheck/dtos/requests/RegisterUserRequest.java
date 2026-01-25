package com.yrsd.medcheck.dtos.requests;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Setter
@Getter
public class RegisterUserRequest {
    private String username;
    private String firstName;
    private String lastName;
    private String middleName;
    private String password;
    private String email;
    private String phoneNumber;
    private String nationalIdentityNumber;
    private String gender;
    private String role;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate dateOfBirth;
}
