package com.drabiter.iona.route;

import spark.Route;

public abstract class BasicRoute implements Route {

    protected final Class<?> clazz;

    public BasicRoute(Class<?> clas) {
        this.clazz = clas;
    }

}
