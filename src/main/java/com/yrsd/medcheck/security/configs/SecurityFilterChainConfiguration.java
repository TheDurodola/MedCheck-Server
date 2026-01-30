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
                        .requestMatchers("/api/v1/consumer/**").hasAuthority("CONSUMER")
                        .requestMatchers("/api/v1/retailer/**").hasAuthority("RETAIL_EMPLOYEE")
                        .requestMatchers("/api/v1/wholesaler/**").hasAuthority("WHOLESALE_EMPLOYEE")
                        .requestMatchers("/api/v1/manufacturer/**").hasAuthority("MANUFACTURER_EMPLOYEE")
                        .requestMatchers("/api/v1/investigator/**").hasAuthority("INVESTIGATOR")
                        .requestMatchers("/api/v1/admin/**").hasAuthority("ADMINISTRATOR")
                        .requestMatchers(HttpMethod.POST, "/api/v1/verification/batch")
                        .hasAnyAuthority("INVESTIGATOR", "MANUFACTURER_EMPLOYEE", "RETAIL_EMPLOYEE", "WHOLESALE_EMPLOYEE")
                        .requestMatchers(HttpMethod.POST, "/api/v1/verification/pack").authenticated()
                        .requestMatchers(HttpMethod.POST, "/api/v1/verification/tablet").authenticated()
                        .requestMatchers(HttpMethod.GET, "/test/live/consumer").hasAuthority("CONSUMER")
                        .requestMatchers(HttpMethod.GET, "/test/live").permitAll()
                        .anyRequest().authenticated())
                .build();

    }
}
