package com.yrsd.medcheck.exceptions;

public class InvalidOrganisationCodeException extends RuntimeException {
    public InvalidOrganisationCodeException(String message) {
        super(message);
    }
}
