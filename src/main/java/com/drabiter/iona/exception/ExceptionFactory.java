package com.drabiter.iona.exception;

public class ExceptionFactory {

    public static IonaException requiredDatabaseProperty() {
        return new IonaException("DatabaseProperty is required");
    }

    public static IonaException notFoundIdField(Throwable t) {
        return new IonaException("Could not find ID field", t);
    }

}
