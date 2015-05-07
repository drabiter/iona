package com.drabiter.iona._meta;

import static org.hamcrest.Matchers.*;

import org.hamcrest.Matcher;

public class Helper {

    public static final int TEST_PORT = 4568;

    public static final Matcher<String> HTML_404 = equalTo("<html><body><h2>404 Not found</h2></body></html>");

    public static final String TEXT_409 = "Conflict resources";

    public static final String TEXT_410_POST = "No resource created";

    public static final String TEXT_410_PUT = "No resource modified";
}
