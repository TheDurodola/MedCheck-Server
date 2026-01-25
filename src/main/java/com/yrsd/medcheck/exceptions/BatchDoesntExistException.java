package com.yrsd.medcheck.exceptions;

public class BatchDoesntExistException extends RuntimeException {
    public BatchDoesntExistException(String message) {
        super(message);
    }
}
