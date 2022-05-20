package com.endava.endabank.exceptions;

public class ErrorBody extends AssertionError{
    public static final String MESSAGE_ERROR_USER_ROLE= "The role was not expected";

    public ErrorBody(String message, Throwable cause){
        super(message, cause);
    }
}
