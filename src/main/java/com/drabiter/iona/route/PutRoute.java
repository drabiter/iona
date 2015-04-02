package com.drabiter.iona.route;

import java.lang.reflect.Field;

import spark.Request;
import spark.Response;

import com.dieselpoint.norm.Query;
import com.drabiter.iona.db.DatabaseSingleton;
import com.drabiter.iona.pojo.ModelCache;
import com.drabiter.iona.pojo.Property;
import com.drabiter.iona.route.BasicRoute;
import com.drabiter.iona.utils.JsonUtil;

public class PutRoute extends BasicRoute {

    public PutRoute(Class<?> clas) {
        super(clas);
    }

    @Override
    public Object handle(Request request, Response response) throws Exception {
        String body = request.body();
        String id = request.params("id");

        if (body == null || id == null) return null;

        Object instance = JsonUtil.get().fromJson(body, clazz);

        if (instance == null) return null;

        Property property = ModelCache.get().cache().get(clazz.getSimpleName().toLowerCase());
        Field idField = property.getIdField();
        idField.setAccessible(true);
        idField.set(instance, castToId(idField.getType(), id));

        Query update = DatabaseSingleton.get().update(instance);

        if (update.getRowsAffected() == 0) return null;

        return body;
    }

    private Object castToId(Class<?> type, String id) {
        if (Long.class.equals(type) || long.class.equals(type)) {
            return Long.parseLong(id);
        }
        return id;
    }

}