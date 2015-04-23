package com.drabiter.iona.utils;

import java.beans.IntrospectionException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import javax.persistence.Id;
import javax.persistence.Transient;

import org.apache.commons.lang3.StringUtils;

import com.drabiter.iona.annotations.MentalModel;
import com.drabiter.iona.exception.ExceptionFactory;
import com.drabiter.iona.exception.IonaException;

public class ModelUtil {

    private static ModelUtil instance = new ModelUtil();

    public static ModelUtil get() {
        return instance;
    }

    public static Field findIdField(Class<?> clazz) throws IntrospectionException, NoSuchFieldException, SecurityException, IonaException {
        for (Field field : clazz.getDeclaredFields()) {
            int modifiers = field.getModifiers();

            if (field.getAnnotation(Transient.class) != null) continue;
            if (Modifier.isFinal(modifiers) || Modifier.isStatic(modifiers)) continue;

            if (field.getAnnotation(Id.class) != null) return field;
        }

        throw ExceptionFactory.notFoundIdField();
    }

    public static String getEndpoint(Class<?> clazz) {
        MentalModel annotation = clazz.getAnnotation(MentalModel.class);

        if (annotation == null || StringUtils.isBlank(annotation.endpoint())) {
            return clazz.getSimpleName().toLowerCase();
        }
        return annotation.endpoint();
    }
}
