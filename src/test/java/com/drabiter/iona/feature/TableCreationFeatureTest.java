package com.drabiter.iona.feature;

import java.sql.SQLException;

import org.junit.After;
import org.junit.Test;

import com.drabiter.iona.Iona;
import com.drabiter.iona._meta.TestUtils;
import com.drabiter.iona._meta.Person;
import com.drabiter.iona.exception.IonaException;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.table.TableUtils;

import static org.junit.Assert.*;

import static spark.SparkBase.*;

public class TableCreationFeatureTest {

    private Iona iona;

    @After
    public void after() {
        stop();
    }

    @Test
    public void testCreateWhenNotExist() throws IonaException, SQLException {
        JdbcConnectionSource connection = new JdbcConnectionSource("jdbc:mysql://localhost:3306/iona", "root", null);
        TableUtils.dropTable(connection, Person.class, true);

        iona = TestUtils.getIona().add(Person.class);

        Dao<Person, ?> dao = iona.getDatabase().getDao(Person.class);

        assertTrue(dao.isTableExists());
    }

    @Test
    public void testCreateWhenExist() throws IonaException, SQLException {
        JdbcConnectionSource connection = new JdbcConnectionSource("jdbc:mysql://localhost:3306/iona", "root", null);
        TableUtils.createTableIfNotExists(connection, Person.class);

        iona = TestUtils.getIona().add(Person.class);

        Dao<Person, ?> dao = iona.getDatabase().getDao(Person.class);

        assertTrue(dao.isTableExists());
    }

}
