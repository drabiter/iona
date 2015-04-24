package com.drabiter.iona.route;

import java.lang.reflect.Field;
import java.net.HttpURLConnection;

import spark.Request;
import spark.Response;

import com.drabiter.iona.db.Database;
import com.drabiter.iona.http.ContentType;
import com.drabiter.iona.model.ModelCache;
import com.drabiter.iona.utils.JsonUtil;
import com.drabiter.iona.utils.ModelUtil;

public class PutRoute<T, I> extends BasicRoute<T, I> {

    public PutRoute(Database database, Class<T> modelClass, Class<I> idClass) {
        super(database, modelClass, idClass);
    }

    @Override
    public Object handle(Request request, Response response) throws Exception {
        String body = request.body();
        String id = request.params("id");

        if (body == null || id == null) return null;

        T instance = JsonUtil.get().fromJson(body, modelClass);

        if (instance == null) return null;

        Field idField = ModelCache.get().cache().get(ModelUtil.getEndpoint(modelClass)).getIdField();
        idField.setAccessible(true);
        idField.set(instance, castId(id));

        int affected = database.update(modelClass, instance);

        if (affected == 1) {
            response(response, HttpURLConnection.HTTP_OK, ContentType.JSON);
            return JsonUtil.get().toJson(instance);
        } else if (affected > 1) {
            response(response, HttpURLConnection.HTTP_CONFLICT, ContentType.TEXT);
            return "Conflict resources";
        } else {
            response(response, HttpURLConnection.HTTP_GONE, ContentType.TEXT);
            return "No resource modified";
        }
    }

}
