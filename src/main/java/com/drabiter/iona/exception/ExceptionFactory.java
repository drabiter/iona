package com.drabiter.iona.exception;

public class ExceptionFactory {

    public static IonaException notFoundIdField(Throwable t) {
        return new IonaException("Could not find ID field", t);
    }

}
