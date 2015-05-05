package com.drabiter.iona.route;

import java.net.HttpURLConnection;

import spark.Request;
import spark.Response;

import com.drabiter.iona.db.Database;
import com.j256.ormlite.dao.Dao;

public class DeleteRoute<T, I> extends BasicRoute<T, I> {

    public DeleteRoute(Database database, Class<T> modelClass, Class<I> idClass) {
        super(database, modelClass, idClass);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Object handle(Request request, Response response) throws Exception {
        String id = request.params("id");

        if (id == null) return null;

        Dao<T, I> dao = (Dao<T, I>) database.getDao(modelClass);
        int affected = dao.deleteById((I) castId(id));

        if (affected == 0) return null;

        response(response, HttpURLConnection.HTTP_NO_CONTENT, null);

        return "";
    }

}
