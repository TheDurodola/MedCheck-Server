package com.yrsd.medcheck.security.exceptions;

import org.springframework.security.core.AuthenticationException;

public class AuthenticationNotSupportedException extends AuthenticationException {
    public AuthenticationNotSupportedException(String message) {
        super(message);
    }
}
