package com.drabiter.iona.http;

public enum ContentType {

    JSON("application/json"),
    TEXT("text/plain");

    private String name;

    ContentType(String name) {
        this.name = name;
    }

    public String asHeader() {
        return name;
    }

}
