package com.drabiter.iona.exception;

@SuppressWarnings("serial")
public class IonaException extends Exception {

    public IonaException(String message) {
        super(message);
    }

    public IonaException(String message, Throwable t) {
        super(message, t);
    }

}
