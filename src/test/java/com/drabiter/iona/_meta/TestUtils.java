package com.drabiter.iona._meta;

import java.lang.reflect.Field;

import com.drabiter.iona.Iona;

public class TestUtils {

    public static void setIonaDatabase(Object instance, Object value) throws Exception {
        Field databaseField = Iona.class.getDeclaredField("database");
        databaseField.setAccessible(true);
        databaseField.set(instance, value);
    }
}
