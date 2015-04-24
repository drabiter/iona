package com.drabiter.iona;

import java.beans.IntrospectionException;
import java.sql.SQLException;

import spark.SparkBase;

import com.drabiter.iona.db.DatabaseProperty;
import com.drabiter.iona.db.Database;
import com.drabiter.iona.exception.ExceptionFactory;
import com.drabiter.iona.exception.IonaException;
import com.drabiter.iona.model.ModelCache;
import com.drabiter.iona.model.Property;
import com.drabiter.iona.route.DeleteRoute;
import com.drabiter.iona.route.GetRoute;
import com.drabiter.iona.route.GetsRoute;
import com.drabiter.iona.route.PostRoute;
import com.drabiter.iona.route.PutRoute;
import com.drabiter.iona.utils.ModelUtil;

import static spark.Spark.*;

public class Iona {

    public static final String DEFAULT_POST = "/%s";
    public static final String DEFAULT_GETS = "/%s";
    public static final String DEFAULT_GET = "/%s/:id";
    public static final String DEFAULT_DELETE = "/%s/:id";
    public static final String DEFAULT_PUT = "/%s/:id";

    private Database database;

    public static Iona init() {
        return new Iona();
    }

    public Iona port(int port) {
        SparkBase.port(port);

        return this;
    }

    public Iona mysql(String url, int port, String db, String user, String password) throws IonaException {
        return mysql(new DatabaseProperty(url, port, db, user, password));
    }

    public Iona mysql(DatabaseProperty dbProperty) throws IonaException {
        if (dbProperty == null) throw ExceptionFactory.noDatabaseProperty();

        try {
            database = new Database(dbProperty);
        } catch (SQLException e) {
            throw ExceptionFactory.failPreparingJdbc(e);
        }

        return this;
    }

    @SuppressWarnings("unchecked")
    public <T, I> Iona add(final Class<T> modelClass) throws IonaException {
        String name = ModelUtil.getEndpoint(modelClass);

        Property property = new Property(name);
        try {
            property.setIdField(ModelUtil.findIdField(modelClass));
        } catch (NoSuchFieldException | SecurityException | IntrospectionException e) {
            throw ExceptionFactory.notFoundIdField(e);
        }

        ModelCache.get().cache().put(name, property);

        Class<I> idClass = (Class<I>) property.getIdField().getType();

        try {
            // TODO test Database / refactor Dao
            database.addDao(modelClass, idClass);
        } catch (SQLException e) {
            throw ExceptionFactory.failPreparingJdbc(e);
        }

        post(String.format(DEFAULT_POST, name), new PostRoute<T, I>(database, modelClass, idClass));

        get(String.format(DEFAULT_GET, name), new GetRoute<T, I>(database, modelClass, idClass));

        get(String.format(DEFAULT_GETS, name), new GetsRoute<T, I>(database, modelClass, idClass));

        delete(String.format(DEFAULT_DELETE, name), new DeleteRoute<T, I>(database, modelClass, idClass));

        put(String.format(DEFAULT_PUT, name), new PutRoute<T, I>(database, modelClass, idClass));

        return this;
    }

    public Database getDatabase() {
        return database;
    }

}
