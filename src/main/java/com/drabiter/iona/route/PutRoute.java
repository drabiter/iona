package com.drabiter.iona.route;

import java.lang.reflect.Field;
import java.net.HttpURLConnection;

import spark.Request;
import spark.Response;

import com.drabiter.iona.db.Database;
import com.drabiter.iona.http.ContentType;
import com.drabiter.iona.model.ModelCache;
import com.drabiter.iona.model.Property;
import com.drabiter.iona.utils.JsonUtil;
import com.drabiter.iona.utils.ModelUtil;
import com.j256.ormlite.dao.Dao;

public class PutRoute<T, I> extends BasicRoute<T, I> {

    public PutRoute(Class<T> modelClass, Class<I> idClass) {
        super(modelClass, idClass);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Object handle(Request request, Response response) throws Exception {
        String body = request.body();
        String id = request.params("id");

        if (body == null || id == null) return null;

        T instance = JsonUtil.get().fromJson(body, modelClass);

        if (instance == null) return null;

        Property property = ModelCache.get().cache().get(ModelUtil.getEndpoint(modelClass));
        Field idField = property.getIdField();
        idField.setAccessible(true);
        idField.set(instance, castId(id));

        Dao<T, I> dao = (Dao<T, I>) Database.get().getDao(modelClass);
        int affected = dao.update(instance);

        if (affected == 0) return null; // TODO return properly

        response(response, HttpURLConnection.HTTP_OK, ContentType.JSON);

        return JsonUtil.get().toJson(instance);
    }

}
