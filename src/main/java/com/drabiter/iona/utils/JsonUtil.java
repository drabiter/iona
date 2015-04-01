package com.drabiter.iona.utils;

import com.google.gson.Gson;

public class JsonUtil {

    private static Gson instance = new Gson();

    public static Gson get() {
        return instance;
    }
}
