package com.example.parks.exceptions;

public class UsernameExistsException extends Exception {

    public UsernameExistsException() {
    }

    public UsernameExistsException(String message) {
        super(message);
    }
}
