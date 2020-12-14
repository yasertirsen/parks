package com.example.parks.exceptions;

public class ParkNotFoundException extends Exception {

    public ParkNotFoundException() {
        super();
    }

    public ParkNotFoundException(String message) {
        super(message);
    }
}
