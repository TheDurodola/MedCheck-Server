package com.yrsd.medcheck.exceptions;

public class OrganizationDoesntExistException extends RuntimeException {
    public OrganizationDoesntExistException(String message) {
        super(message);
    }
}
