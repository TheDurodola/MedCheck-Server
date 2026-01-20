package com.yrsd.medcheck.security.configs;

import com.yrsd.medcheck.security.filters.CustomAuthenticationFilter;
import com.yrsd.medcheck.security.filters.CustomAuthorizationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityFilterChainConfiguration {

    private final CustomAuthenticationFilter customAuthenticationFilter;
    private final CustomAuthorizationFilter  customAuthorizationFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .addFilterAt(customAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterAfter(customAuthorizationFilter, CustomAuthenticationFilter.class)
                .authorizeHttpRequests((c) -> c
                        .requestMatchers(HttpMethod.POST, "/api/v1/auth/signup").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/v1/auth/signin").permitAll()
                        .requestMatchers("/api/v1/consumer/**").hasAllAuthorities("CONSUMER", "ACTIVE")
                        .requestMatchers("/api/v1/retailer/**").hasAuthority("RETAILER")
                        .requestMatchers("/api/v1/wholesaler/**").hasAuthority("WHOLESALER")
                        .requestMatchers("/api/v1/manufacturer/**").hasAuthority("MANUFACTURER")
                        .requestMatchers("/api/v1/investigator/**").hasAuthority("INVESTIGATOR")
                        .requestMatchers(HttpMethod.POST, "/api/v1/verification/batch")
                                .hasAnyAuthority("INVESTIGATOR", "MANUFACTURER", "RETAILER", "WHOLESALER")
                        .requestMatchers(HttpMethod.POST, "/api/v1/verification/pack").authenticated()
                        .requestMatchers(HttpMethod.POST, "/api/v1/verification/tablet").authenticated()
                        .requestMatchers(HttpMethod.GET, "/test/live").permitAll()
                        .requestMatchers(HttpMethod.GET, "/test/live/consumer").hasAuthority("CONSUMER")
                        .anyRequest().authenticated())
                .build();

    }
}
