package com.drabiter.iona.exception;

import java.sql.SQLException;

public class ExceptionFactory {

    public static IonaException noDatabaseProperty() {
        return new IonaException("DatabaseProperty is required");
    }

    public static IonaException notFoundIdField(Throwable t) {
        t.printStackTrace();
        return new IonaException("Could not find @Id field", t);
    }

    public static IonaException notFoundIdField() {
        return new IonaException("@Id field is missing");
    }

    public static IonaException notSupportedIdField() {
        return new IonaException("Unsupported type for @Id field");
    }

    public static IonaException failPreparingJdbc(SQLException e) {
        e.printStackTrace();
        return new IonaException("Could not open database connection", e);
    }

    public static IonaException failDatabase(SQLException e) {
        e.printStackTrace();
        return new IonaException("Database error", e);
    }

}
