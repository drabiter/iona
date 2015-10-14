package com.drabiter.iona.route;

import spark.Request;
import spark.Response;

import com.drabiter.iona.IonaResource;
import com.drabiter.iona.service.pojo.PojoService;
import com.drabiter.iona.util.JsonUtil;
import com.google.gson.JsonSyntaxException;

public class PutRoute<T, I> extends BasicRoute<T, I> {

    public PutRoute(IonaResource iona, Class<T> modelClass, Class<I> idClass) {
        super(iona, modelClass, idClass);
    }

    @Override
    public Object handle(Request request, Response response) throws Exception {
        String body = request.body();
        String id = request.params("id");

        T instance = null;

        try {
            instance = JsonUtil.get().fromJson(body, modelClass);
        } catch (JsonSyntaxException jse) {
            return response400(response, jse.getMessage());
        } finally {
            if (instance == null) return response400(response);
        }

        PojoService.setId(instance, id);

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
