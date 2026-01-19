package com.yrsd.medcheck.security.configs;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.filter.OncePerRequestFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityFilterChainConfiguration {

    private final OncePerRequestFilter oncePerRequestFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .addFilterAt(oncePerRequestFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests((c) -> c.requestMatchers(HttpMethod.POST, "/api/v1/auth/signup").permitAll())
                .authorizeHttpRequests(c->c.requestMatchers(HttpMethod.POST, "/api/v1/auth/signin").permitAll())
                .authorizeHttpRequests(c->c.requestMatchers(HttpMethod.GET, "/test/live").permitAll())
                .authorizeHttpRequests(c->c.anyRequest().authenticated())
                .build();
    }
}
