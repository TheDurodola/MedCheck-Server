package com.yrsd.medcheck.exceptions;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class UsernameAlreadyExistException extends RuntimeException {

    public UsernameAlreadyExistException(String username) {
        super(username);
    }
}
