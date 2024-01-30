package com.darwgom.taskapi.domain.exceptions;

public class EmailAlreadyRegisteredException extends RuntimeException {
    public EmailAlreadyRegisteredException(String email) {
        super("The email already registered: " + email);
    }
}
