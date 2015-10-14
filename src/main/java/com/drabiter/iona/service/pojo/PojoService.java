package com.drabiter.iona.service.pojo;

import java.beans.IntrospectionException;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.drabiter.iona.exception.ExceptionFactory;
import com.drabiter.iona.exception.IonaException;
import com.drabiter.iona.model.Property;
import com.drabiter.iona.util.PojoUtil;

public class PojoService {

    private static Map<Class<?>, Property> properties = new HashMap<>();

    public static Property register(Class<?> modelClass) throws IonaException {
        Property property = new Property();

        property.setModelClass(modelClass);
        property.setEndpoint(PojoUtil.getEndpoint(modelClass));

        try {
            Field idField = PojoUtil.findIdField(modelClass);

            property.setIdField(idField);
            property.setIdClass(idField.getType());
        } catch (NoSuchFieldException | SecurityException | IntrospectionException e) {
            throw ExceptionFactory.notFoundIdField(e);
        }

        properties.put(modelClass, property);

        return property;
    }

    public static Property get(Class<?> modelClass) {
        return properties.get(modelClass);
    }

    public static Set<Class<?>> getClasses() {
        return properties.keySet();
    }

    public static void clear() {
        properties.clear();
    }

    public static void setId(Object instance, String id) throws Exception {
        Field idField = PojoService.get(instance.getClass()).getIdField();
        idField.setAccessible(true);
        idField.set(instance, PojoUtil.castId(id, idField.getType()));
    }

    public static Object getId(Object instance) throws IllegalArgumentException, IllegalAccessException {
        Field idField = PojoService.get(instance.getClass()).getIdField();
        idField.setAccessible(true);

        return idField.get(instance);
    }

}
