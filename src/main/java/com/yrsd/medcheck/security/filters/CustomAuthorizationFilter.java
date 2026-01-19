package com.yrsd.medcheck.security.filters;

import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.yrsd.medcheck.data.models.enums.Role;
import com.yrsd.medcheck.security.dtos.responses.UserAccountResponse;
import com.yrsd.medcheck.services.UserAccountService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.access.intercept.AuthorizationFilter;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.*;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomAuthorizationFilter extends OncePerRequestFilter {

    private final UserAccountService  userAccountService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        try {
            if (isPublicApi(request)) {
                filterChain.doFilter(request, response);
                return;
            }

            String requestHeader = request.getHeader(AUTHORIZATION);
            if (requestHeader == null || !requestHeader.startsWith("Bearer ")) {
                log.debug("request header: {}", requestHeader);
                createErrorResponse(response);
            } else {
                String jwt = requestHeader.split(" ")[1];
                DecodedJWT token = JWT.decode(jwt);
                String username = token.getSubject();
                UserAccountResponse userAccountBy = userAccountService.getUserAccountBy(username);

                Role role = userAccountBy.getRole();
                List<SimpleGrantedAuthority> accountRole = new ArrayList<>();
                accountRole.add(new SimpleGrantedAuthority(role.name()));
                Authentication auth = new UsernamePasswordAuthenticationToken(userAccountBy.getUsername(), null, accountRole);
                SecurityContextHolder.getContext().setAuthentication(auth);
                log.info("Authorization Completed");
                filterChain.doFilter(request, response);
            }
        } catch (IOException | JWTDecodeException e) {
            log.error("Error processing request", e);
            createErrorResponse(response);
        }
    }

    private void createErrorResponse(HttpServletResponse response) throws IOException {
        Map<String, String> error = new HashMap<>();
        error.put("error", "Invalid JWT");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.getOutputStream().write(error.toString().getBytes());
        response.flushBuffer();
    }

    private static boolean isPublicApi(HttpServletRequest request) {
        return request.getServletPath().equals("/api/v1/auth/signin") || request.getServletPath().equals("/api/v1/auth/signup");
    }
}
