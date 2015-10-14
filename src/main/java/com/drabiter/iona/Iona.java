package com.drabiter.iona;

import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import spark.SparkBase;
import spark.route.RouteMatcherFactory;

import com.drabiter.iona.db.Database;
import com.drabiter.iona.exception.ExceptionFactory;
import com.drabiter.iona.exception.IonaException;
import com.drabiter.iona.model.Property;
import com.drabiter.iona.service.http.RestService;
import com.drabiter.iona.service.pojo.PojoService;

public class Iona implements IonaResource {

    final Logger logger = LoggerFactory.getLogger(Iona.class);

    private Database database;

    public static Iona init(String url, String user, String password) throws IonaException {
        return new Iona(url, user, password);
    }

    public Iona(String url, String user, String password) throws IonaException {
        try {
            database = new Database(url, user, password);
            logger.info("== Connected to {}", url);
        } catch (SQLException e) {
            logger.error("Could not establish database connection", e);
            throw ExceptionFactory.failPreparingJdbc(e);
        }
    }

    public Iona port(int port) {
        SparkBase.port(port);
        logger.info("== Listening on {}", port);

        return this;
    }

    public <T, I> Iona add(final Class<T> modelClass) throws IonaException {
        Property property = PojoService.register(modelClass);
        logger.info("== Added {}", modelClass);

        try {
            database.createTable(modelClass);
            database.addDao(modelClass, property.getIdClass());
        } catch (SQLException e) {
            throw ExceptionFactory.failDatabase(e);
        }

        return this;
    }

    public void start() {
        for (Class<?> modelClass : PojoService.getClasses()) {
            Property property = PojoService.get(modelClass);
            RestService.register(this, property.getEndpoint(), modelClass, property.getIdClass());
        }

        SparkBase.awaitInitialization();
        logger.info(">> Started Iona");
    }

    public void stop() {
        SparkBase.stop();
        logger.info("<< Stopped Iona");
    }

    public static void clearRoutes() {
        RouteMatcherFactory.get().clearRoutes();
    }

    @Override
    public Database getDatabase() {
        return database;
    }

}
