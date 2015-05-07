package com.drabiter.iona.route;

import spark.Request;
import spark.Response;

import com.drabiter.iona.IonaResource;
import com.drabiter.iona.model.Pojo;
import com.drabiter.iona.util.JsonUtil;

public class PutRoute<T, I> extends BasicRoute<T, I> {

    public PutRoute(IonaResource iona, Class<T> modelClass, Class<I> idClass) {
        super(iona, modelClass, idClass);
    }

    @Override
    public Object handle(Request request, Response response) throws Exception {
        String body = request.body();
        String id = request.params("id");

        if (id == null) {
            return response404(response);
        }

        T instance = JsonUtil.get().fromJson(body, modelClass);

        if (instance == null) {
            return response400(response);
        }

        Pojo.setId(instance, id);

        int affected = iona.getDatabase().update(modelClass, instance);

        if (affected == 1) {
            return response200(response, JsonUtil.get().toJson(instance));
        } else if (affected > 1) {
            return response409(response, "Conflict resources");
        } else {
            return response410(response, "No resource modified");
        }
    }

}
