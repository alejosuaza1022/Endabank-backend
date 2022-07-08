package com.endava.endabank.exceptions.custom;


public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String msg) {
        super(msg);
    }

}
