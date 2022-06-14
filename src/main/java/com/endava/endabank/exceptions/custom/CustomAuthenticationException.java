package com.endava.endabank.exceptions.custom;

public class CustomAuthenticationException extends org.springframework.security.core.AuthenticationException {
    public CustomAuthenticationException(String msg) {
        super(msg);
    }
}
