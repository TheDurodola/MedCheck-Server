package com.yrsd.medcheck.controllers;

import com.yrsd.medcheck.dtos.requests.RegisterUserRequest;
import com.yrsd.medcheck.services.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping(value = "/signup", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createUser(@Valid @RequestBody RegisterUserRequest request) {
        Map<String, Object> response = new HashMap<>();
        response.put("message", "User account created successfully");
        response.put("data", authService.registerUser(request));
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

}
