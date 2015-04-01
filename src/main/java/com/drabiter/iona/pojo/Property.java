package com.drabiter.iona.pojo;

import java.lang.reflect.Field;

public class Property {

    private String name;

    private Field idField;

    public Property(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Field getIdField() {
        return idField;
    }

    public void setIdField(Field idField) {
        this.idField = idField;
    }
}
