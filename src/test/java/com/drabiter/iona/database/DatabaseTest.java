package com.drabiter.iona.database;

import org.junit.Test;

import com.drabiter.iona.Iona;
import com.drabiter.iona.exception.IonaException;

import static org.junit.Assert.*;

public class DatabaseTest {

    @Test(expected = IonaException.class)
    public void testNullDatabaseConfiguration() throws IonaException {
        Iona.init(null, null, null);
        Iona.init(null, "root", "");
        Iona.init("jdbc:mysql://localhost:3306/iona", null, "");
        Iona.init("jdbc:mysql://localhost:3306/iona", "root", null);
    }

    @Test
    public void testDatabaseConfiguration() throws IonaException {
        Iona iona = Iona.init("jdbc:mysql://DB_URL:4000/DB_DATABASE", "root", "");

        assertEquals("jdbc:mysql://DB_URL:4000/DB_DATABASE", iona.getDatabase().getConnectionPool().getUrl());
    }

}
