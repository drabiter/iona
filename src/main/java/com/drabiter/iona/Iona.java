package com.drabiter.iona;

import java.beans.IntrospectionException;

import com.drabiter.iona.db.DatabaseProperty;
import com.drabiter.iona.db.DatabaseSingleton;
import com.drabiter.iona.exception.IonaException;
import com.drabiter.iona.pojo.PojoCache;
import com.drabiter.iona.pojo.Property;
import com.drabiter.iona.route.DeleteRoute;
import com.drabiter.iona.route.GetRoute;
import com.drabiter.iona.route.GetsRoute;
import com.drabiter.iona.route.PostRoute;
import com.drabiter.iona.route.PutRoute;
import com.drabiter.iona.utils.PojoUtil;

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

    public Iona db(String driver, String url, String db, String user, String password) {
        return db(new DatabaseProperty(driver, url, db, user, password));
    }

    public Iona db(DatabaseProperty dbProperty) {
        if (dbProperty == null) return this;

        System.setProperty(DatabaseProperty.NORM_DATA_SOURCE_CLASS_NAME, dbProperty.getDriver());
        System.setProperty(DatabaseProperty.NORM_SERVER_NAME, dbProperty.getUrl());
        System.setProperty(DatabaseProperty.NORM_DATABASE_NAME, dbProperty.getDatabase());
        System.setProperty(DatabaseProperty.NORM_USER, dbProperty.getUser());
        System.setProperty(DatabaseProperty.NORM_PASSWORD, dbProperty.getPassword());

        DatabaseSingleton.setProperty(dbProperty);

        return this;
    }

    public Iona addModel(final Class<?> clazz) throws IonaException {
        String name = clazz.getSimpleName().toLowerCase();

        Property property = new Property(name);
        try {
            property.setIdField(PojoUtil.findIdField(clazz));
        } catch (NoSuchFieldException | SecurityException | IntrospectionException e) {
            throw new IonaException("Could not find ID field", e);
        }

        PojoCache.get().cache().put(name, property);

        post(String.format(DEFAULT_POST, name), new PostRoute(clazz));

        get(String.format(DEFAULT_GET, name), new GetRoute(clazz));

        get(String.format(DEFAULT_GETS, name), new GetsRoute(clazz));

        delete(String.format(DEFAULT_DELETE, name), new DeleteRoute(clazz));

        put(String.format(DEFAULT_PUT, name), new PutRoute(clazz));

        return this;
    }

}
