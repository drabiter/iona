package com.drabiter.iona.route;

import spark.Request;
import spark.Response;

import com.drabiter.iona.IonaResource;
import com.drabiter.iona.http.Header;
import com.drabiter.iona.model.Pojo;
import com.drabiter.iona.util.JsonUtil;
import com.google.gson.JsonSyntaxException;

public class PostRoute<T, I> extends BasicRoute<T, I> {

    public PostRoute(IonaResource iona, Class<T> modelType, Class<I> idType) {
        super(iona, modelType, idType);
    }

    @Override
    public Object handle(Request request, Response response) throws Exception {
        String body = request.body();

        T instance = null;

        try {
            instance = JsonUtil.get().fromJson(body, modelClass);
        } catch (JsonSyntaxException jse) {
            return response400(response, jse.getMessage());
        } finally {
            if (instance == null) return response400(response);
        }

        int affected = iona.getDatabase().create(modelClass, instance);

        if (affected == 1) {
            response.header(Header.Location.value(), request.url() + "/" + Pojo.getId(instance));

            return response201(response, instance);
        } else if (affected > 1) {
            return response409(response, "Conflict resources");
        } else {
            return response410(response, "No resource created");
        }
    }

}
