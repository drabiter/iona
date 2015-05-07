package com.drabiter.iona.integration;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import spark.route.RouteMatcherFactory;

import com.drabiter.iona.Iona;
import com.drabiter.iona._meta.Helper;
import com.drabiter.iona._meta.Person;
import com.drabiter.iona._meta.TestUtils;
import com.drabiter.iona.db.Database;
import com.drabiter.iona.exception.IonaException;
import com.drabiter.iona.util.JsonUtil;
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

    private static Iona iona;

    private static Database originalDatabase;

    @BeforeClass
    public static void setup() throws Exception {
        iona = Iona.init("jdbc:mysql://localhost:3306/iona", "root", "").port(Helper.TEST_PORT);
        originalDatabase = iona.getDatabase();
    }

    @AfterClass
    public static void tearDown() {
        stop();
    }

    @Before
    public void before() throws IonaException {
        RestAssured.port = Helper.TEST_PORT;
        RouteMatcherFactory.get().clearRoutes();
    }

    @After
    public void after() throws Exception {
        TestUtils.setIonaDatabase(iona, originalDatabase);
        RestAssured.reset();
    }

    @Test
    public void testPutNotCreated() throws Exception {
        sharedTestPutFail(410, Helper.TEXT_410_PUT, 0);
    }

    @Test
    public void testPutCreatedMultiple() throws Exception {
        sharedTestPutFail(409, Helper.TEXT_409, 2);
    }

    private void sharedTestPutFail(int expectedCode, String expectedMessage, int mockValue) throws Exception {
        Database spiedDatabase = spy(originalDatabase);
        doReturn(mockValue).when(spiedDatabase).update(any(), any());

        TestUtils.setIonaDatabase(iona, spiedDatabase);

        iona.add(Person.class);
        Thread.sleep(1500);

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
