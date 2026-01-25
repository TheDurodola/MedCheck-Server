package com.yrsd.medcheck.exceptions;

public class PackDoesntExistException extends RuntimeException {
    public PackDoesntExistException(String s) {
        super(s);
    }
}
