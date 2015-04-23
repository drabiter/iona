package com.drabiter.iona.route;

import java.net.HttpURLConnection;
import java.util.List;

import spark.Request;
import spark.Response;

import com.drabiter.iona.db.Database;
import com.drabiter.iona.http.ContentType;
import com.drabiter.iona.utils.JsonUtil;

public class GetsRoute<T, I> extends BasicRoute<T, I> {

    public GetsRoute(Class<T> modelClass, Class<I> idClass) {
        super(modelClass, idClass);
    }

    @Override
    public Object handle(Request request, Response response) throws Exception {
        List<T> results = Database.get().getDao(modelClass).queryForAll();

        response(response, HttpURLConnection.HTTP_OK, ContentType.JSON);

        return JsonUtil.get().toJson(results);
    }

}
