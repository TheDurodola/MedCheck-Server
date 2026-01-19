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
                .authorizeHttpRequests((c) ->
                        c.requestMatchers(HttpMethod.POST, "/api/v1/auth/signup").permitAll())
                .authorizeHttpRequests(c->
                        c.requestMatchers(HttpMethod.POST, "/api/v1/auth/signin").permitAll())
                .authorizeHttpRequests(c->
                        c.requestMatchers("/api/v1/consumer/**").hasAllAuthorities("CONSUMER", "ACTIVE"))
                .authorizeHttpRequests(c->
                        c.requestMatchers("/api/v1/retailer/**").hasAuthority("RETAILER"))
                .authorizeHttpRequests(c->
                        c.requestMatchers("/api/v1/wholesaler/**").hasAuthority("WHOLESALER"))
                .authorizeHttpRequests(c->
                        c.requestMatchers("/api/v1/manufacturer/**").hasAuthority("MANUFACTURER"))
                .authorizeHttpRequests(c->
                        c.requestMatchers("/api/v1/investigator/**").hasAuthority("INVESTIGATOR"))
                .authorizeHttpRequests(c->
                        c.requestMatchers(HttpMethod.POST, "/api/v1/verification/batch")
                                .hasAnyAuthority("INVESTIGATOR", "MANUFACTURER", "RETAILER", "WHOLESALER"))
                .authorizeHttpRequests(c->
                        c.requestMatchers(HttpMethod.GET, "/test/live").permitAll())
                .authorizeHttpRequests(c->
                        c.requestMatchers(HttpMethod.GET, "/test/live/consumer").hasAuthority("CONSUMER"))
                .authorizeHttpRequests(c->c.anyRequest().authenticated())
                .build();

    }
}
