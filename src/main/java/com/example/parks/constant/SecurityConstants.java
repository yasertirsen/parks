package com.example.parks.constant;

public class SecurityConstants {

    public static final long EXPIRATION_TIME = 86400000; //1 DAY in milliseconds
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String JWT_TOKEN_HEADER = "Jwt-Token";
    public static final String TOKEN_CANNOT_BE_VERIFIED = "Token cannot be verified.";
    public static final String ISSUER = "PROGRAD";
    public static final String PROGRAD_ADMINISTRATION = "Setup Portal";
    public static final String AUTHORITIES = "authorities";
    public static final String FORBIDDEN_MESSAGE = "You need to log in to access this page";
    public static final String ACCESS_DENIED = "You do not have permission to access this page";
    public static final String OPTIONS_HTTP_METHOD = "OPTIONS";
    public static final String[] PUBLIC_URLS = {"/api/register", "/api/login", "/api/verification/**", "/api/all", "/"};
}

