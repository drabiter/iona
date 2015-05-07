package com.drabiter.iona.route;

import java.sql.SQLException;

import spark.Request;
import spark.Response;

import com.drabiter.iona.IonaResource;
import com.drabiter.iona.util.JsonUtil;

public class PostRoute<T, I> extends BasicRoute<T, I> {

    public PostRoute(IonaResource iona, Class<T> modelType, Class<I> idType) {
        super(iona, modelType, idType);
    }

    @Override
    public Object handle(Request request, Response response) throws SQLException {
        String body = request.body();

        T instance = JsonUtil.get().fromJson(body, modelClass);

        if (instance == null) {
            return response400(response);
        }

        int affected = iona.getDatabase().create(modelClass, instance);

        if (affected == 1) {
            return response201(response, instance);
        } else if (affected > 1) {
            return response409(response, "Conflict resources");
        } else {
            return response410(response, "No resource created");
        }
    }

}
