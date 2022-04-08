package com.endava.endabank.exceptions.customExceptions;

import java.nio.file.AccessDeniedException;

public class BadDataException extends AccessDeniedException {
    public BadDataException(String msg) {
        super(msg);
    }
}
