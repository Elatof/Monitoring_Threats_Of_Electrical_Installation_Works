package com.korbiak.service.security;

public class AuthException extends RuntimeException{
    public AuthException(String message) {
        super(message);
    }
}
