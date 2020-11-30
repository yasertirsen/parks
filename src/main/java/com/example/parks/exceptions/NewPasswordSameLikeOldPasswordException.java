package com.example.parks.exceptions;

public class NewPasswordSameLikeOldPasswordException extends Exception {

    public NewPasswordSameLikeOldPasswordException(String message) {
        super(message);
    }
}
