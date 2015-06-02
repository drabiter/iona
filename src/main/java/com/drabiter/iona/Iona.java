package com.drabiter.iona;

import java.sql.SQLException;

import spark.SparkBase;
import spark.route.RouteMatcherFactory;

import com.drabiter.iona.db.Database;
import com.drabiter.iona.exception.ExceptionFactory;
import com.drabiter.iona.exception.IonaException;
import com.drabiter.iona.http.Rest;
import com.drabiter.iona.model.Pojo;
import com.drabiter.iona.model.Property;

public class Iona implements IonaResource {

    private Database database;

    public static Iona init(String url, String user, String password) throws IonaException {
        return new Iona(url, user, password);
    }

    public Iona(String url, String user, String password) throws IonaException {
        try {
            database = new Database(url, user, password);
        } catch (SQLException e) {
            throw ExceptionFactory.failPreparingJdbc(e);
        }
    }

    public Iona port(int port) {
        SparkBase.port(port);

        return this;
    }

    public <T, I> Iona add(final Class<T> modelClass) throws IonaException {
        Property property = Pojo.register(modelClass);

        try {
            database.createTable(modelClass);
            database.addDao(modelClass, property.getIdClass());
        } catch (SQLException e) {
            throw ExceptionFactory.failDatabase(e);
        }

        return this;
    }

    public void start() {
        for (Class<?> modelClass : Pojo.getClasses()) {
            Property property = Pojo.get(modelClass);
            Rest.register(this, property.getEndpoint(), modelClass, property.getIdClass());
        }

        SparkBase.awaitInitialization();
    }

    public static void stop() {
        SparkBase.stop();
    }

    public static void clearRoutes() {
        RouteMatcherFactory.get().clearRoutes();
    }

    @Override
    public Database getDatabase() {
        return database;
    }

}
