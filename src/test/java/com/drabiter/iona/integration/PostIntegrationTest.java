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

import static com.jayway.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;
import static spark.SparkBase.*;

public class PostIntegrationTest {

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
        reset();
    }

    @Test
    public void testPostNotCreated() throws Exception {
        sharedTest(410, Helper.TEXT_410_POST, 0);
    }

    @Test
    public void testPostCreatedMultiple() throws Exception {
        sharedTest(409, Helper.TEXT_409, 2);
    }

    private void sharedTest(int expectedCode, String expectedMessage, int mockValue) throws Exception {
        Database spiedDatabase = spy(originalDatabase);
        doReturn(mockValue).when(spiedDatabase).create(any(), any());

        TestUtils.setIonaDatabase(iona, spiedDatabase);

        iona.add(Person.class);
        Thread.sleep(1500);

        Person person = new Person();
        person.setFirstName("A");
        person.setLastName("B");

        given().body(JsonUtil.get().toJson(person)).when().post("/person")
                .then().assertThat().statusCode(expectedCode).contentType(ContentType.TEXT).body(equalTo(expectedMessage));
        verify(spiedDatabase, times(1)).create(any(), any());
    }
}
