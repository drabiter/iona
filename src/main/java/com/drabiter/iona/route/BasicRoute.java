package com.drabiter.iona.route;

import java.net.HttpURLConnection;

import org.apache.commons.lang3.StringUtils;

import spark.Response;
import spark.Route;

import com.drabiter.iona.IonaResource;
import com.drabiter.iona.http.ContentType;
import com.drabiter.iona.util.JsonUtil;

public abstract class BasicRoute<T, I> implements Route {

    protected final IonaResource iona;
    protected final Class<T> modelClass;
    protected final Class<I> idClass;

    public BasicRoute(IonaResource iona, Class<T> modelClass, Class<I> idClass) {
        this.iona = iona;
        this.modelClass = modelClass;
        this.idClass = idClass;
    }

    protected Object response200(Response response, Object body) {
        response.status(HttpURLConnection.HTTP_OK);
        response.type(ContentType.JSON.asHeader());
        return (body instanceof String) ? body : JsonUtil.get().toJson(body);
    }

    protected Object response201(Response response, Object body) {
        response.status(HttpURLConnection.HTTP_CREATED);
        response.type(ContentType.JSON.asHeader());
        return (body instanceof String) ? body : JsonUtil.get().toJson(body);
    }

    protected Object response204(Response response) {
        response.status(HttpURLConnection.HTTP_NO_CONTENT);
        return StringUtils.EMPTY;
    }

    protected Object response400(Response response) {
        response.status(HttpURLConnection.HTTP_BAD_REQUEST);
        return null;
    }

    protected Object response404(Response response) {
        response.status(HttpURLConnection.HTTP_NOT_FOUND);
        return null;
    }

    protected Object response409(Response response, String body) {
        response.status(HttpURLConnection.HTTP_CONFLICT);
        response.type(ContentType.TEXT.asHeader());
        return body;
    }

    protected Object response410(Response response, String body) {
        response.status(HttpURLConnection.HTTP_GONE);
        response.type(ContentType.TEXT.asHeader());
        return body;
    }

}
