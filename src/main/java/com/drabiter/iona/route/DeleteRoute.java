package com.drabiter.iona.route;

import spark.Request;
import spark.Response;

import com.drabiter.iona.IonaResource;
import com.drabiter.iona.util.PojoUtil;
import com.j256.ormlite.dao.Dao;

public class DeleteRoute<T, I> extends BasicRoute<T, I> {

    public DeleteRoute(IonaResource iona, Class<T> modelClass, Class<I> idClass) {
        super(iona, modelClass, idClass);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Object handle(Request request, Response response) throws Exception {
        String id = request.params("id");

        if (id == null) {
            return response404(response);
        }

        Dao<T, I> dao = (Dao<T, I>) iona.getDatabase().getDao(modelClass);
        int affected = dao.deleteById((I) PojoUtil.castId(id, idClass));

        if (affected == 0) {
            return response404(response);
        }

        return response204(response);
    }

}
