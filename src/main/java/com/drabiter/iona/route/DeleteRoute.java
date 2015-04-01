package com.drabiter.iona.route;

import java.util.List;

import spark.Request;
import spark.Response;

import com.dieselpoint.norm.Query;
import com.drabiter.iona.db.DatabaseSingleton;

public class DeleteRoute extends BasicRoute {

    public DeleteRoute(Class<?> clas) {
        super(clas);
    }

    @Override
    public Object handle(Request request, Response response) throws Exception {
        String param = request.params("id");

        if (param == null) return null;

        List<?> results = DatabaseSingleton.get().where("id=?", param).results(clazz);

        if (results == null || results.size() == 0) return null;

        Query delete = DatabaseSingleton.get().delete(results.get(0));

        if (delete.getRowsAffected() == 0) return null;

        return "";
    }

}
