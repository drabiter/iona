package com.drabiter.iona.configuration;

import org.junit.Test;

import com.drabiter.iona.Iona;
import com.drabiter.iona.db.Database;
import com.drabiter.iona.exception.IonaException;

import static org.junit.Assert.*;

public class DatabaseConfigurationTest {

    @Test(expected = IonaException.class)
    public void testNullDatabaseConfiguration() throws IonaException {
        Iona.init().mysql(null);
    }

    @Test
    public void testDatabaseConfiguration() throws IonaException {
        String host = "DB_URL";
        int port = 4000;
        String database = "DB_DATABASE";
        String user = "DB_USER";
        String password = "DB_PASSW";

        Iona.init().mysql(host, port, database, user, password);

        assertEquals("jdbc:mysql://DB_URL:4000/DB_DATABASE", Database.get().getConnectionPool().getUrl());
    }

}
