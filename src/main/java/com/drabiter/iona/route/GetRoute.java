package com.drabiter.iona.route;

import java.net.HttpURLConnection;

import spark.Request;
import spark.Response;

import com.drabiter.iona.db.Database;
import com.drabiter.iona.http.ContentType;
import com.drabiter.iona.utils.JsonUtil;
import com.j256.ormlite.dao.Dao;

public class GetRoute<T, I> extends BasicRoute<T, I> {

    public GetRoute(Database database, Class<T> modelType, Class<I> idType) {
        super(database, modelType, idType);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Object handle(Request request, Response response) throws Exception {
        String id = request.params("id");

        if (id == null) return null;

        Dao<T, I> dao = (Dao<T, I>) database.getDao(modelClass);
        T entity = dao.queryForId((I) castId(id));

        if (entity == null) return null;

        response(response, HttpURLConnection.HTTP_OK, ContentType.JSON);

        return JsonUtil.get().toJson(entity);
    }

}
