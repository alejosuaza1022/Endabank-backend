package com.endava.endabank.exceptions.customExceptions;

import java.nio.file.AccessDeniedException;

public class ActionNotAllowedException extends AccessDeniedException {
    public ActionNotAllowedException(String msg) {
        super(msg);
    }
}
