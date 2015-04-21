package com.drabiter.iona.route;

import java.net.HttpURLConnection;

import spark.Request;
import spark.Response;

import com.drabiter.iona.db.DatabaseSingleton;
import com.drabiter.iona.utils.JsonUtil;

public class GetRoute extends BasicRoute {

    public GetRoute(Class<?> clazz) {
        super(clazz);
    }

    @Override
    public Object handle(Request request, Response response) throws Exception {
        String id = request.params("id");

        if (id == null) return null;

        Object entity = DatabaseSingleton.get().where("id=?", id).first(clazz);

        if (entity == null) return null;

        response.status(HttpURLConnection.HTTP_OK);
        response.type("application/json");

        return JsonUtil.get().toJson(entity);
    }

}
