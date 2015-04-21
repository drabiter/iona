package com.drabiter.iona;

import java.beans.IntrospectionException;

import spark.SparkBase;

import com.drabiter.iona.db.DatabaseProperty;
import com.drabiter.iona.db.DatabaseSingleton;
import com.drabiter.iona.exception.ExceptionFactory;
import com.drabiter.iona.exception.IonaException;
import com.drabiter.iona.pojo.ModelCache;
import com.drabiter.iona.pojo.Property;
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

    public static Iona init() {
        return new Iona();
    }

    public Iona port(int port) {
        SparkBase.port(port);

        return this;
    }

    public Iona db(String driver, String url, String db, String user, String password) throws IonaException {
        return db(new DatabaseProperty(driver, url, db, user, password));
    }

    public Iona mysql(String url, String db, String user, String password) throws IonaException {
        return db(new DatabaseProperty(DatabaseProperty.MYSQL_DATASOURCE, url, db, user, password));
    }

    public Iona db(DatabaseProperty dbProperty) throws IonaException {
        if (dbProperty == null) throw ExceptionFactory.requiredDatabaseProperty();

        System.setProperty(DatabaseProperty.NORM_DATA_SOURCE_CLASS_NAME, dbProperty.getDriver());
        System.setProperty(DatabaseProperty.NORM_SERVER_NAME, dbProperty.getUrl());
        System.setProperty(DatabaseProperty.NORM_DATABASE_NAME, dbProperty.getDatabase());
        System.setProperty(DatabaseProperty.NORM_USER, dbProperty.getUser());
        System.setProperty(DatabaseProperty.NORM_PASSWORD, dbProperty.getPassword());

        DatabaseSingleton.setProperty(dbProperty);

        return this;
    }

    public Iona add(final Class<?> clazz) throws IonaException {
        String name = ModelUtil.getEndpoint(clazz);

        Property property = new Property(name);
        try {
            property.setIdField(ModelUtil.findIdField(clazz));
        } catch (NoSuchFieldException | SecurityException | IntrospectionException e) {
            throw ExceptionFactory.notFoundIdField(e);
        }

        ModelCache.get().cache().put(name, property);

        post(String.format(DEFAULT_POST, name), new PostRoute(clazz));

        get(String.format(DEFAULT_GET, name), new GetRoute(clazz));

        get(String.format(DEFAULT_GETS, name), new GetsRoute(clazz));

        delete(String.format(DEFAULT_DELETE, name), new DeleteRoute(clazz));

        put(String.format(DEFAULT_PUT, name), new PutRoute(clazz));

        return this;
    }

}
