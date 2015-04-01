package com.drabiter.iona.db;

public class DatabaseProperty {

    public static final String NORM_PASSWORD = "norm.password";

    public static final String NORM_USER = "norm.user";

    public static final String NORM_DATABASE_NAME = "norm.databaseName";

    public static final String NORM_SERVER_NAME = "norm.serverName";

    public static final String NORM_DATA_SOURCE_CLASS_NAME = "norm.dataSourceClassName";

    private String driver;

    private String url;

    private String database;

    private String user;

    private String password;

    public DatabaseProperty(String dbDriver, String dbUrl, String database, String dbUser, String dbPassword) {
        this.driver = dbDriver;
        this.url = dbUrl;
        this.database = database;
        this.user = dbUser;
        this.password = dbPassword;
    }

    public DatabaseProperty() {
    }

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDatabase() {
        return database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
