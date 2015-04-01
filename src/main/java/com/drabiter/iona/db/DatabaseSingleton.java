package com.drabiter.iona.db;

import com.dieselpoint.norm.Database;

public class DatabaseSingleton {

    private static final Database instance = new Database();

    private static DatabaseProperty property = new DatabaseProperty();

    public DatabaseSingleton(DatabaseProperty dbProp) {
        DatabaseSingleton.property = dbProp;
    }

    public DatabaseSingleton() {
    }

    public static Database get() {
        return instance;
    }

    public static DatabaseProperty getProperty() {
        return property;
    }

    public static synchronized void setProperty(DatabaseProperty property) {
        DatabaseSingleton.property = property;
    }

}
