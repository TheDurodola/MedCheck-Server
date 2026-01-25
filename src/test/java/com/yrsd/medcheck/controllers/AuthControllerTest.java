package com.yrsd.medcheck.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yrsd.medcheck.services.interfaces.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(AuthController.class)
class AuthControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;


    private AuthService authService;

}