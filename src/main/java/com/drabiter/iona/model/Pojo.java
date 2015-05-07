package com.drabiter.iona.model;

import java.beans.IntrospectionException;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import com.drabiter.iona.exception.ExceptionFactory;
import com.drabiter.iona.exception.IonaException;
import com.drabiter.iona.util.ModelUtil;

public class Pojo {

    private static Map<Class<?>, Property> properties = new HashMap<>();

    public static Property register(Class<?> modelClass) throws IonaException {
        Property property = new Property();
        property.setModelClass(modelClass);
        property.setEndpoint(ModelUtil.getEndpoint(modelClass));

        try {
            Field idField = ModelUtil.findIdField(modelClass);

            property.setIdField(idField);
            property.setIdClass(idField.getType());
        } catch (NoSuchFieldException | SecurityException | IntrospectionException e) {
            throw ExceptionFactory.notFoundIdField(e);
        }

        properties.put(modelClass, property);

        return property;
    }

    public static void setId(Object instance, String id) throws Exception {
        Field idField = Pojo.get(instance.getClass()).getIdField();
        idField.setAccessible(true);
        idField.set(instance, ModelUtil.castId(id, idField.getType()));
    }

    public static Property get(Class<?> modelClass) {
        return properties.get(modelClass);
    }
}
