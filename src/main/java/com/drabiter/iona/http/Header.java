package com.drabiter.iona.http;

public enum Header {
    Location("Location");

    private String value;

    private Header(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }
}
