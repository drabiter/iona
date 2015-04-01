package com.drabiter.iona.exception;

@SuppressWarnings("serial")
public class IonaException extends Exception {

    public IonaException(String message, Throwable e) {
        super(message, e);
    }

}
