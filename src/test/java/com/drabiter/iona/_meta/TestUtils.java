package com.drabiter.iona._meta;

import java.lang.reflect.Field;

import org.hamcrest.Matcher;

import com.drabiter.iona.Iona;
import com.drabiter.iona.exception.IonaException;

import static org.hamcrest.Matchers.*;

public class TestUtils {

    public static final int TEST_PORT = 4568;

    public static final Matcher<String> MATCHER_HTML_404 = equalTo("<html><body><h2>404 Not found</h2></body></html>");

    public static final String TEXT_409 = "Conflict resources";

    public static final String TEXT_410_POST = "No resource created";

    public static final String TEXT_410_PUT = "No resource modified";

    public static Iona getIona() throws IonaException {
        return Iona.init("jdbc:hsqldb:mem:iona", "root", "").port(TEST_PORT);
    }

    public static void setIonaDatabase(Object instance, Object value) throws Exception {
        Field databaseField = Iona.class.getDeclaredField("database");
        databaseField.setAccessible(true);
        databaseField.set(instance, value);
    }
}
