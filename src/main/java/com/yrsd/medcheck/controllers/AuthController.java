package com.yrsd.medcheck.controllers;

import com.yrsd.medcheck.dtos.requests.RegisterUserRequest;
import com.yrsd.medcheck.services.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping(value = "/signup")
    public ResponseEntity<?> createUser(@Valid @RequestBody RegisterUserRequest request) {
        log.info("Creating user");
        Map<String, Object> response = new HashMap<>();
        response.put("message", "User account created successfully");
        response.put("data", authService.registerUser(request));
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

}
