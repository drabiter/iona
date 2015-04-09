package com.drabiter.iona.configuration;

import org.junit.Test;

import com.drabiter.iona.Iona;
import com.drabiter.iona.db.DatabaseProperty;
import com.drabiter.iona.db.DatabaseSingleton;
import com.drabiter.iona.exception.IonaException;

import static org.junit.Assert.*;

public class DatabaseConfigurationTest {

    @Test(expected = IonaException.class)
    public void testNullDatabaseConfiguration() throws IonaException {
        Iona.init().db(null);
    }

    @Test
    public void testDatabaseConfiguration() throws IonaException {
        String driver = "DB_DRIVER";
        String url = "DB_URL";
        String database = "DB_DATABASE";
        String user = "DB_USER";
        String password = "DB_PASSW";

        Iona.init().db(driver, url, database, user, password);

        assertDatabaseProperties(driver, url, database, user, password);
    }

    @Test
    public void testMysqlConfiguration() throws IonaException {
        String url = "DB_URL";
        String database = "DB_DATABASE";
        String user = "DB_USER";
        String password = "DB_PASSW";

        Iona.init().mysql(url, database, user, password);

        assertDatabaseProperties(DatabaseProperty.MYSQL_DATASOURCE, url, database, user, password);
    }

    private void assertDatabaseProperties(Object driver, Object url, Object database, Object user, Object password) {
        assertEquals(driver, DatabaseSingleton.getProperty().getDriver());
        assertEquals(url, DatabaseSingleton.getProperty().getUrl());
        assertEquals(database, DatabaseSingleton.getProperty().getDatabase());
        assertEquals(user, DatabaseSingleton.getProperty().getUser());
        assertEquals(password, DatabaseSingleton.getProperty().getPassword());

        assertEquals(driver, System.getProperty(DatabaseProperty.NORM_DATA_SOURCE_CLASS_NAME));
        assertEquals(url, System.getProperty(DatabaseProperty.NORM_SERVER_NAME));
        assertEquals(database, System.getProperty(DatabaseProperty.NORM_DATABASE_NAME));
        assertEquals(user, System.getProperty(DatabaseProperty.NORM_USER));
        assertEquals(password, System.getProperty(DatabaseProperty.NORM_PASSWORD));
    }
}
