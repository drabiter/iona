package com.drabiter.iona.http;

public enum ContentType {

    JSON("application/json"),
    TEXT("text/plain");

    private String value;

    ContentType(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }

}
