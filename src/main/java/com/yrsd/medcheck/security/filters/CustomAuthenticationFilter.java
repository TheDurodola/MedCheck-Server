package com.yrsd.medcheck.security.filters;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.yrsd.medcheck.security.auth.CustomAuthentication;
import com.yrsd.medcheck.security.dtos.requests.SignInRequest;
import com.yrsd.medcheck.security.dtos.responses.SignInResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.io.InputStream;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Component
@Primary
public class CustomAuthenticationFilter extends OncePerRequestFilter {

    private final AuthenticationManager authenticationManager;
    private final ObjectMapper objectMapper;
    private final String signingKey;

    @Value("${jwt.duration}")
    private long jwtValidationLength;

    public CustomAuthenticationFilter( AuthenticationManager authenticationManager, ObjectMapper objectMapper, @Value("${jwt.signing.key}")  String signingKey) {
        this.authenticationManager = authenticationManager;
        this.objectMapper = objectMapper;
        this.signingKey = signingKey;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String path = request.getServletPath();

        boolean isSignInPath = path.equals("/api/v1/auth/signin");
        if (!isSignInPath){
            filterChain.doFilter(request,response);
            return;
        }
        log.info("Authentication Initiated");
        try {
            InputStream inputStream = request.getInputStream();
            SignInRequest signInRequest = objectMapper.readValue(inputStream, SignInRequest.class);
            signInRequest.setUsername(signInRequest.getUsername().toLowerCase());
            Authentication authentication = new CustomAuthentication(signInRequest.getUsername(), signInRequest.getPassword());
            Authentication result = authenticationManager.authenticate(authentication);
            String jwt = JWT.create()
                    .withIssuer("medcheck")
                    .withSubject(Objects.requireNonNull(result.getPrincipal()).toString())
                    .withIssuedAt(Date.from(Instant.now()))
                    .withExpiresAt(Date.from(Instant.now().plusSeconds(jwtValidationLength)))
                    .sign(Algorithm.HMAC256(signingKey.getBytes()));


            SignInResponse signInResponse = new SignInResponse(jwt, result.getAuthorities().iterator().next().getAuthority());

            response.setContentType("application/json");
            response.setStatus(HttpServletResponse.SC_OK);
            response.getOutputStream()
                    .write(objectMapper.writeValueAsBytes(signInResponse));
            log.info("JWT token created for user: {}", result.getPrincipal().toString());
            response.flushBuffer();
        } catch (IOException e) {
            log.error("IOException while sending JWT token to user: {}", e.getMessage());
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            error.put("message", "Something went wrong...");
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.setContentType("application/json");
            response.getOutputStream().write(objectMapper.writeValueAsBytes(error));
            response.flushBuffer();
        } catch (AuthenticationException | JWTCreationException | IllegalArgumentException e) {
            log.info("JWTException while sending JWT token to user: {}", e.getMessage());
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            error.put("message", "Invalid Username or Password");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getOutputStream().write(objectMapper.writeValueAsBytes(error));
            response.flushBuffer();
        }
    }
}



