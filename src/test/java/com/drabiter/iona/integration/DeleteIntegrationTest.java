package com.drabiter.iona.integration;

import java.sql.SQLException;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import spark.SparkBase;

import com.drabiter.iona.Iona;
import com.drabiter.iona._meta.Person;
import com.drabiter.iona._meta.TestUtils;
import com.j256.ormlite.table.TableUtils;
import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;

import static com.jayway.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class DeleteIntegrationTest {

    private static Iona iona;

    @BeforeClass
    public static void setup() throws Exception {
        iona = TestUtils.iona().add(Person.class);
    }

    @AfterClass
    public static void tearDown() throws SQLException {
        TableUtils.dropTable(iona.getDatabase().getConnectionPool(), Person.class, false);
        SparkBase.stop();
    }

    @Before
    public void before() throws Exception {
        TableUtils.clearTable(iona.getDatabase().getConnectionPool(), Person.class);
        RestAssured.port = TestUtils.TEST_PORT;
        Iona.clearRoutes();
    }

    @After
    public void after() throws Exception {
        reset();
    }

    @Test
    public void testDelete() throws Exception {
        Person person = new Person();
        person.setId(1L);
        person.setFirstName("Hongo");
        person.setLastName("Takeshi");

        iona.start();

        iona.getDatabase().getDao(Person.class).create(person);

        delete("/person/" + person.getId()).then().assertThat().statusCode(204).contentType(isEmptyString()).body(isEmptyString());
    }

    @Test
    public void testDeleteNotExist() throws Exception {
        iona.start();

        delete("/person/1").then().assertThat().statusCode(404).contentType(ContentType.HTML).body(TestUtils.MATCHER_HTML_404);
    }
}
