package com.drabiter.iona.service.http;

import com.drabiter.iona.IonaResource;
import com.drabiter.iona.route.DeleteRoute;
import com.drabiter.iona.route.GetRoute;
import com.drabiter.iona.route.PostRoute;
import com.drabiter.iona.route.PutRoute;

import static spark.Spark.*;

public class RestService {

    public static final String DEFAULT_POST = "/%s";
    public static final String DEFAULT_GETS = "/%s";
    public static final String DEFAULT_GET = "/%s/:id";
    public static final String DEFAULT_DELETE = "/%s/:id";
    public static final String DEFAULT_PUT = "/%s/:id";

    public static <T, I> void register(IonaResource iona, String endpoint, Class<T> modelClass, Class<I> idClass) {
        get("ping", (req, res) -> "pong");

        get(String.format(DEFAULT_GETS, endpoint), new GetRoute<T, I>(iona, modelClass, idClass));

        get(String.format(DEFAULT_GET, endpoint), new GetRoute<T, I>(iona, modelClass, idClass));

        post(String.format(DEFAULT_POST, endpoint), new PostRoute<T, I>(iona, modelClass, idClass));

        delete(String.format(DEFAULT_DELETE, endpoint), new DeleteRoute<T, I>(iona, modelClass, idClass));

        put(String.format(DEFAULT_PUT, endpoint), new PutRoute<T, I>(iona, modelClass, idClass));
    }
}
