package com.endava.endabank.exceptions.custom;

public class BadDataException extends RuntimeException {
    public BadDataException(String msg) {
        super(msg);
    }
}
