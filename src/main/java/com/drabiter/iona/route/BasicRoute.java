package com.drabiter.iona.route;

import spark.Response;
import spark.Route;

import com.drabiter.iona.http.ContentType;

public abstract class BasicRoute implements Route {

    protected final Class<?> clazz;

    public BasicRoute(Class<?> clas) {
        this.clazz = clas;
    }

    protected void response(Response response, int status, ContentType type) {
        response.status(status);
        response.type(type.asHeader());
    }

}
