package com.example.parks.exceptions;

import com.example.parks.constant.ErrorConstants;
import com.example.parks.model.HttpCustomResponse;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.nio.file.AccessDeniedException;
import java.util.Objects;

import static com.example.parks.constant.ErrorConstants.*;
import static com.example.parks.constant.ErrorConstants.UNEXPECTED_VALUE;
import static com.example.parks.constant.ErrorConstants.USERNAME_ALREADY_EXISTS;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.METHOD_NOT_ALLOWED;
import static org.springframework.http.HttpStatus.NOT_IMPLEMENTED;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@RestControllerAdvice
public class ParksExceptionHandler {

    @ExceptionHandler(ParkNotFoundException.class)
    public ResponseEntity<HttpCustomResponse> parkNotFoundException() {
        return createHttpResponse(BAD_REQUEST, COULD_NOT_FIND_PARK_PLAYGROUND_TO_UPDATE);
    }

    @ExceptionHandler(ParsingException.class)
    public ResponseEntity<HttpCustomResponse> parsingException() {
        return createHttpResponse(BAD_REQUEST, ERROR_PARSING_PARK_WEBPAGE);
    }

    @ExceptionHandler(UsernameExistsException.class)
    public ResponseEntity<HttpCustomResponse> usernameExistsException() {
        return createHttpResponse(CONFLICT, USERNAME_ALREADY_EXISTS);
    }

    @ExceptionHandler(EmailExistsException.class)
    public ResponseEntity<HttpCustomResponse> emailExistsException() {
        return createHttpResponse(CONFLICT, EMAIL_ALREADY_EXISTS);
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<HttpCustomResponse> illegalStateException() {
        return createHttpResponse(NOT_IMPLEMENTED, UNEXPECTED_VALUE);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<HttpCustomResponse> accessDeniedException() {
        return createHttpResponse(FORBIDDEN, NOT_ENOUGH_PERMISSION);
    }

    @ExceptionHandler(InvalidDataFormatException.class)
    public ResponseEntity<HttpCustomResponse> invalidDataFormat() {
        return createHttpResponse(BAD_REQUEST, INVALID_DATA_FORMAT);
    }


    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<HttpCustomResponse> invalidCredentials() {
        return createHttpResponse(UNAUTHORIZED, INVALID_CREDENTIALS);
    }


    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<HttpCustomResponse> methodNotSupportedException(HttpRequestMethodNotSupportedException exception) {
        HttpMethod supportedMethod = Objects.requireNonNull(exception.getSupportedHttpMethods()).iterator().next();
        return createHttpResponse(METHOD_NOT_ALLOWED, String.format(METHOD_IS_NOT_ALLOWED, supportedMethod));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<HttpCustomResponse> internalServerErrorException() {
        return createHttpResponse(INTERNAL_SERVER_ERROR, INTERNAL_SERVER_ERROR_MSG);
    }


    private ResponseEntity<HttpCustomResponse> createHttpResponse(HttpStatus httpStatus, String message) {
        HttpCustomResponse httpCustomResponse = new HttpCustomResponse(httpStatus.value(), httpStatus, httpStatus.getReasonPhrase().toUpperCase(), message);

        return new ResponseEntity<>(httpCustomResponse, httpStatus);
    }

}
