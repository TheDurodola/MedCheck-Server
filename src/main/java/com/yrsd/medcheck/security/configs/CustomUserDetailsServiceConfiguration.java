package com.yrsd.medcheck.security.configs;

import com.yrsd.medcheck.data.models.UserAccount;
import com.yrsd.medcheck.exceptions.AccountNotFoundException;
import com.yrsd.medcheck.security.dtos.responses.UserAccountResponse;
import com.yrsd.medcheck.services.AuthService;
import com.yrsd.medcheck.services.UserAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;
import java.util.List;

@Configuration
@RequiredArgsConstructor
public class CustomUserDetailsServiceConfiguration implements UserDetailsService {

    private final UserAccountService accountService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            UserAccountResponse response = accountService.getUserAccountBy(username);
            List<SimpleGrantedAuthority> authorities = new ArrayList<SimpleGrantedAuthority>();
            authorities.add(new SimpleGrantedAuthority(response.getRole().name()));
            return new User(response.getUsername(), response.getPassword(), authorities);
        } catch (AccountNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
