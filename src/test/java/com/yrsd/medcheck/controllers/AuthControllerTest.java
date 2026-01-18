package com.yrsd.medcheck.controllers;

import com.yrsd.medcheck.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

//@WebMvcTest(AuthController.class)
//class AuthControllerTest {
//    @Autowired
//    private MockMvc mockMvc; // Tool to send requests
//
//    @Autowired
//    private ObjectMapper objectMapper; // Tool to convert Objects <-> JSON
//
//    @MockBean
//    private AuthService authService;
//
//