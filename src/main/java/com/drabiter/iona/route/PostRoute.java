package com.drabiter.iona.route;

import java.net.HttpURLConnection;

import spark.Request;
import spark.Response;

import com.drabiter.iona.db.Database;
import com.drabiter.iona.http.ContentType;
import com.drabiter.iona.utils.JsonUtil;
import com.j256.ormlite.dao.Dao;

public class PostRoute<T, I> extends BasicRoute<T, I> {

    public PostRoute(Class<T> modelType, Class<I> idType) {
        super(modelType, idType);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Object handle(Request request, Response response) throws Exception {
        String body = request.body();

        if (body == null) return null;

        T instance = JsonUtil.get().fromJson(body, modelClass);

        if (instance == null) return null;

        Dao<T, I> dao = (Dao<T, I>) Database.get().getDao(modelClass);
        dao.create(instance);

        // TODO if affect != 1

        response(response, HttpURLConnection.HTTP_CREATED, ContentType.JSON);

        return JsonUtil.get().toJson(instance);
    }

}
