package com.drabiter.iona.route;

import java.util.List;

import spark.Request;
import spark.Response;

import com.drabiter.iona.db.DatabaseSingleton;
import com.drabiter.iona.utils.JsonUtil;

public class GetRoute extends BasicRoute {

    public GetRoute(Class<?> clazz) {
        super(clazz);
    }

    @Override
    public Object handle(Request request, Response response) throws Exception {
        String param = request.params("id");

        if (param == null) return null;

        List<?> results = DatabaseSingleton.get().where("id=?", param).results(clazz);

        if (results == null || results.size() == 0) return null;

        // TODO content-type
        return JsonUtil.get().toJson(results.get(0));
    }

}
