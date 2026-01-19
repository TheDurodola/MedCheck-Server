package com.yrsd.medcheck.exceptions;

public class InvalidNationalIdentityNumberException extends RuntimeException {
    public InvalidNationalIdentityNumberException(String message) {
        super(message);
    }
}
