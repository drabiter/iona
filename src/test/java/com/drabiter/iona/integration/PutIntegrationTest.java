package com.drabiter.iona.integration;

import java.lang.reflect.Field;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;

import com.drabiter.iona.Iona;
import com.drabiter.iona._meta.Person;
import com.drabiter.iona.db.Database;
import com.drabiter.iona.db.DatabaseProperty;
import com.drabiter.iona.exception.IonaException;
import com.drabiter.iona.utils.JsonUtil;
import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.ValidatableResponse;

import static com.jayway.restassured.RestAssured.*;
import static com.jayway.restassured.path.json.JsonPath.*;
import static org.hamcrest.Matchers.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;
import static spark.SparkBase.*;

public class PutIntegrationTest {

    protected static final int TEST_PORT = 4568;

    @AfterClass
    public static void tearDown() {
    }

    @Before
    public void before() throws IonaException {
        RestAssured.port = TEST_PORT;
    }

    @After
    public void after() {
        RestAssured.reset();
        stop();
    }

    @Test
    public void testPutNotCreated() throws Exception {
        sharedTestPutFail(410, "No resource modified", 0);
    }

    @Test
    public void testPutCreatedMultiple() throws Exception {
        sharedTestPutFail(409, "Conflict resources", 2);
    }

    private void sharedTestPutFail(int expectedCode, String expectedMessage, int mockValue) throws Exception {
        DatabaseProperty dbProperty = new DatabaseProperty("localhost", 3306, "iona", "root", "");
        Iona iona = Iona.init().port(TEST_PORT).mysql(dbProperty);

        Database database = iona.getDatabase();
        Database spiedDatabase = spy(database);
        doReturn(mockValue).when(spiedDatabase).update(any(), any());

        Field databaseField = Iona.class.getDeclaredField("database");
        databaseField.setAccessible(true);
        databaseField.set(iona, spiedDatabase);

        iona.add(Person.class);

        Person person = new Person();
        person.setFirstName("A");
        person.setLastName("B");

        ValidatableResponse response = given().body(JsonUtil.get().toJson(person)).when().post("/person")
                .then().assertThat().statusCode(201);

        given().body(JsonUtil.get().toJson(person)).when().put("/person/" + from(response.extract().asString()).get("id"))
                .then().assertThat().statusCode(expectedCode).contentType(ContentType.TEXT).body(equalTo(expectedMessage));
        verify(spiedDatabase, times(1)).create(any(), any());
    }

}
