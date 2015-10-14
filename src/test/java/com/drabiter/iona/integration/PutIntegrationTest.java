package com.drabiter.iona.integration;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.drabiter.iona.Iona;
import com.drabiter.iona._meta.TestUtils;
import com.drabiter.iona._meta.Person;
import com.drabiter.iona.db.Database;
import com.drabiter.iona.exception.IonaException;
import com.drabiter.iona.util.JsonUtil;
import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;

import static com.drabiter.iona._meta.IsSamePerson.*;
import static com.jayway.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;
import static spark.SparkBase.*;

public class PutIntegrationTest {

    private static Iona iona;

    private static Database originalDatabase;

    @BeforeClass
    public static void setup() throws Exception {
        iona = TestUtils.iona().add(Person.class);
        originalDatabase = iona.getDatabase();
    }

    @AfterClass
    public static void tearDown() {
        stop();
    }

    @Before
    public void before() throws IonaException {
        RestAssured.port = TestUtils.TEST_PORT;
        Iona.clearRoutes();
    }

    @After
    public void after() throws Exception {
        TestUtils.setIonaDatabase(iona, originalDatabase);
        reset();
    }

    @Test
    public void testPutNotExist() throws Exception {
        iona.start();

        Person person = new Person();
        person.setId(1L);
        person.setFirstName("A");
        person.setLastName("B");

        iona.getDatabase().getDao(Person.class).create(person);

        given().body(JsonUtil.get().toJson(person)).when().put("/person/" + (person.getId() + 999))
                .then().assertThat().statusCode(410).contentType(ContentType.TEXT).body(equalTo(TestUtils.TEXT_410_PUT));
    }

    @Test
    public void testPutSameBody() throws Exception {
        iona.start();

        Person person = new Person();
        person.setId(1L);
        person.setFirstName("A");
        person.setLastName("B");

        iona.getDatabase().getDao(Person.class).create(person);

        given().body(JsonUtil.get().toJson(person)).when().put("/person/" + person.getId())
                .then().assertThat().statusCode(200).contentType(ContentType.JSON).body(isSamePerson(person));
    }

    @Test
    public void testPutEmptyJson() throws Exception {
        iona.start();

        Person person = new Person();
        person.setId(1L);
        person.setFirstName("A");
        person.setLastName("B");

        iona.getDatabase().getDao(Person.class).create(person);

        person.setFirstName(null);
        person.setLastName(null);

        given().body("{}").when().put("/person/" + person.getId())
                .then().assertThat().statusCode(200).contentType(ContentType.JSON).body(isSamePerson(person));
    }

    @Test
    public void testPutEmptyBody() throws Exception {
        iona.start();

        given().body("").when().put("/person/999")
                .then().assertThat().statusCode(400).contentType(ContentType.HTML).body(notNullValue());

    }

    @Test
    public void testPutNoBody() throws Exception {
        iona.start();

        given().when().put("/person/999")
                .then().assertThat().statusCode(400).contentType(ContentType.HTML).body(notNullValue());
    }

    @Test
    public void testPutMalformedJson() throws Exception {
        iona.start();

        given().body("aaa").when().put("/person/999")
                .then().assertThat().statusCode(400).body(notNullValue());
    }

    @Test
    public void testPutNotCreated() throws Exception {
        sharedTestPutFail(410, TestUtils.TEXT_410_PUT, 0);
    }

    @Test
    public void testPutCreatedMultiple() throws Exception {
        sharedTestPutFail(409, TestUtils.TEXT_409, 2);
    }

    private void sharedTestPutFail(int expectedCode, String expectedMessage, int mockValue) throws Exception {
        Database spiedDatabase = spy(originalDatabase);
        doReturn(mockValue).when(spiedDatabase).update(any(), any());

        TestUtils.setIonaDatabase(iona, spiedDatabase);

        iona.start();

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
