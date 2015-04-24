package com.drabiter.iona.route;

import java.net.HttpURLConnection;
import java.sql.SQLException;

import spark.Request;
import spark.Response;

import com.drabiter.iona.db.Database;
import com.drabiter.iona.http.ContentType;
import com.drabiter.iona.utils.JsonUtil;

public class PostRoute<T, I> extends BasicRoute<T, I> {

    public PostRoute(Database database, Class<T> modelType, Class<I> idType) {
        super(database, modelType, idType);
    }

    @Override
    public Object handle(Request request, Response response) throws SQLException {
        String body = request.body();

        if (body == null) return null;

        T instance = JsonUtil.get().fromJson(body, modelClass);

        if (instance == null) return null;

        int affected = database.create(modelClass, instance);

        if (affected == 1) {
            response(response, HttpURLConnection.HTTP_CREATED, ContentType.JSON);
            return JsonUtil.get().toJson(instance);
        } else if (affected > 1) {
            response(response, HttpURLConnection.HTTP_CONFLICT, ContentType.TEXT);
            return "Conflict resources";
        } else {
            response(response, HttpURLConnection.HTTP_GONE, ContentType.TEXT);
            return "No resource created";
        }
    }

}
