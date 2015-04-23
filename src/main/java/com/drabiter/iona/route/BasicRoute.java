package com.drabiter.iona.route;

import spark.Response;
import spark.Route;

import com.drabiter.iona.exception.ExceptionFactory;
import com.drabiter.iona.exception.IonaException;
import com.drabiter.iona.http.ContentType;

public abstract class BasicRoute<T, I> implements Route {

    protected final Class<T> modelClass;
    protected final Class<I> idClass;

    public BasicRoute(Class<T> modelClass, Class<I> idClass) {
        this.modelClass = modelClass;
        this.idClass = idClass;
    }

    protected void response(Response response, int status, ContentType type) {
        response.status(status);
        response.type(type.asHeader());
    }

    protected Object castId(String id) throws IonaException {
        if (Long.class == idClass || long.class == idClass) {
            return Long.parseLong(id);
        } else if (String.class == idClass) {
            return id;
        }
        throw ExceptionFactory.notSupportedIdField();
    }

}
