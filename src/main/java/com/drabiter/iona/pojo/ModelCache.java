package com.drabiter.iona.pojo;

import java.util.HashMap;

public class ModelCache {

    private HashMap<String, Property> cache = new HashMap<String, Property>();

    private static ModelCache instance = new ModelCache();

    public static ModelCache get() {
        return instance;
    }

    public HashMap<String, Property> cache() {
        return cache;
    }

}
