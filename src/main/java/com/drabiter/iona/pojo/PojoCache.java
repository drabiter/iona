package com.drabiter.iona.pojo;

import java.util.HashMap;

public class PojoCache {

    private HashMap<String, Property> cache = new HashMap<String, Property>();

    private static PojoCache instance = new PojoCache();

    public static PojoCache get() {
        return instance;
    }

    public HashMap<String, Property> cache() {
        return cache;
    }

}
