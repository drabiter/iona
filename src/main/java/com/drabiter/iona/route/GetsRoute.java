package com.drabiter.iona.route;

import java.util.List;

import spark.Request;
import spark.Response;

import com.drabiter.iona.db.DatabaseSingleton;
import com.drabiter.iona.utils.JsonUtil;

public class GetsRoute extends BasicRoute {

    public GetsRoute(Class<?> clazz) {
        super(clazz);
    }

    @Override
    public Object handle(Request request, Response response) throws Exception {
        List<?> results = DatabaseSingleton.get().results(clazz);

        return JsonUtil.get().toJson(results);
    }

}
