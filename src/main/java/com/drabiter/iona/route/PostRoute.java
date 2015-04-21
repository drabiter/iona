package com.drabiter.iona.route;

import java.net.HttpURLConnection;

import spark.Request;
import spark.Response;

import com.drabiter.iona.db.DatabaseSingleton;
import com.drabiter.iona.http.ContentType;
import com.drabiter.iona.utils.JsonUtil;

public class PostRoute extends BasicRoute {

    public PostRoute(Class<?> clazz) {
        super(clazz);
    }

    @Override
    public Object handle(Request request, Response response) throws Exception {
        String body = request.body();

        if (body == null) return null;

        Object instance = JsonUtil.get().fromJson(body, clazz);

        if (instance == null) return null;

        DatabaseSingleton.get().insert(instance);

        response(response, HttpURLConnection.HTTP_CREATED, ContentType.JSON);

        return JsonUtil.get().toJson(instance);
    }

}
