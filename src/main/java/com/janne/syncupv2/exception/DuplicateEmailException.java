package com.janne.syncupv2.exception;

public class DuplicateEmailException extends RuntimeException {
    public DuplicateEmailException(String email) {
        super(email);
    }
}