package com.drabiter.iona.model;

import java.lang.reflect.Field;

public class Property {

    private String endpoint;

    private Field idField;

    private Class<?> modelClass;

    private Class<?> idClass;

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public Field getIdField() {
        return idField;
    }

    public void setIdField(Field idField) {
        this.idField = idField;
    }

    public Class<?> getModelClass() {
        return modelClass;
    }

    public void setModelClass(Class<?> modelClass) {
        this.modelClass = modelClass;
    }

    public Class<?> getIdClass() {
        return idClass;
    }

    public void setIdClass(Class<?> idClass) {
        this.idClass = idClass;
    }
}
