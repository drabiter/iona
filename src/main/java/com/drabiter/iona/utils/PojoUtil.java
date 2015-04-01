package com.drabiter.iona.utils;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import javax.persistence.Id;
import javax.persistence.Transient;

public class PojoUtil {

    private static PojoUtil instance = new PojoUtil();

    public static PojoUtil get() {
        return instance;
    }

    public static Field findIdField(Class<?> clazz) throws IntrospectionException, NoSuchFieldException, SecurityException {
        for (Field field : clazz.getDeclaredFields()) {
            int modifiers = field.getModifiers();

            if (Modifier.isPrivate(modifiers) || Modifier.isFinal(modifiers) || Modifier.isStatic(modifiers)) continue;
            if (field.getAnnotation(Transient.class) != null) continue;

            if (field.getAnnotation(Id.class) != null) return field;
        }

        BeanInfo beanInfo = Introspector.getBeanInfo(clazz, Object.class);
        PropertyDescriptor[] descriptors = beanInfo.getPropertyDescriptors();
        for (PropertyDescriptor descriptor : descriptors) {
            Method getter = descriptor.getReadMethod();

            if (getter.getAnnotation(Id.class) != null) {
                return clazz.getDeclaredField(descriptor.getName());
            }
        }

        return null;
    }

}
