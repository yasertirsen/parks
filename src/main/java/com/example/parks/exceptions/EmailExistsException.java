package com.example.parks.exceptions;

public class EmailExistsException extends Exception {

    public EmailExistsException() {
    }

    public EmailExistsException(String message) {
        super(message);
    }
}
