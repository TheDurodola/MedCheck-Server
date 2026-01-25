package com.yrsd.medcheck.exceptions;

public class DrugDoesntExistException extends RuntimeException {
    public DrugDoesntExistException(String message) {
        super(message);
    }
}
