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
import static com.drabiter.iona._meta.IsSamePerson.*;

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
    public void testPutNotExist() throws Exception {
        iona.add(Person.class);
        Thread.sleep(1500);

        Person person = new Person();
        person.setId(1L);
        person.setFirstName("A");
        person.setLastName("B");

        iona.getDatabase().getDao(Person.class).create(person);

        given().body(JsonUtil.get().toJson(person)).when().put("/person/" + (person.getId() + 999))
                .then().assertThat().statusCode(410).contentType(ContentType.TEXT).body(equalTo(Helper.TEXT_410_PUT));
    }

    @Test
    public void testPutSameBody() throws Exception {
        iona.add(Person.class);
        Thread.sleep(1500);

        Person person = new Person();
        person.setId(1L);
        person.setFirstName("A");
        person.setLastName("B");

        iona.getDatabase().getDao(Person.class).create(person);

        given().body(JsonUtil.get().toJson(person)).when().put("/person/" + person.getId())
                .then().assertThat().statusCode(200).contentType(ContentType.JSON).body(isSamePerson(person));
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
        person.setId(1L);
        person.setFirstName("A");
        person.setLastName("B");

        iona.getDatabase().getDao(Person.class).create(person);

        given().body(JsonUtil.get().toJson(person)).when().put("/person/" + person.getId())
                .then().assertThat().statusCode(expectedCode).contentType(ContentType.TEXT).body(equalTo(expectedMessage));
        verify(spiedDatabase, times(1)).update(any(), any());
    }
}
