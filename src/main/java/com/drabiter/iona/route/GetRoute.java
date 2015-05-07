package com.drabiter.iona.route;

import java.util.List;

import spark.Request;
import spark.Response;

import com.drabiter.iona.IonaResource;
import com.drabiter.iona.util.ModelUtil;
import com.j256.ormlite.dao.Dao;

public class GetRoute<T, I> extends BasicRoute<T, I> {

    public GetRoute(IonaResource iona, Class<T> modelType, Class<I> idType) {
        super(iona, modelType, idType);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Object handle(Request request, Response response) throws Exception {
        String id = request.params("id");

        if (id == null) {
            List<T> results = iona.getDatabase().getDao(modelClass).queryForAll();

            return response200(response, results);
        }

        Dao<T, I> dao = (Dao<T, I>) iona.getDatabase().getDao(modelClass);
        T entity = dao.queryForId((I) ModelUtil.castId(id, idClass));

        if (entity == null) {
            return response404(response);
        }

        return response200(response, entity);
    }

}
