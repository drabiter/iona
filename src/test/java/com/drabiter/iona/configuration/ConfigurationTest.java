package com.drabiter.iona.configuration;

import org.junit.Test;

import com.drabiter.iona.Iona;
import com.drabiter.iona.db.DatabaseSingleton;

import static org.junit.Assert.*;

public class ConfigurationTest {

    @Test
    public void testDatabaseConfiguration() {
        String driver = "DB_DRIVER";
        String url = "DB_URL";
        String database = "DB_DATABASE";
        String user = "DB_USER";
        String password = "DB_PASSW";

        Iona.init().db(driver, url, database, user, password);

        assertEquals(driver, DatabaseSingleton.getProperty().getDriver());
        assertEquals(url, DatabaseSingleton.getProperty().getUrl());
        assertEquals(database, DatabaseSingleton.getProperty().getDatabase());
        assertEquals(user, DatabaseSingleton.getProperty().getUser());
        assertEquals(password, DatabaseSingleton.getProperty().getPassword());
    }

}
