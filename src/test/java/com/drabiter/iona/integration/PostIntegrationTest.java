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
import com.drabiter.iona.http.Header;
import com.drabiter.iona.util.JsonUtil;
import com.j256.ormlite.table.TableUtils;
import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;

import static com.drabiter.iona._meta.IsSamePerson.*;
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

        TableUtils.clearTable(iona.getDatabase().getConnectionPool(), Person.class);
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
    public void testPostDuplicate() throws Exception {
        iona.add(Person.class);
        Thread.sleep(1500);

        Person person = new Person();
        person.setId(1L);
        person.setFirstName("A");
        person.setLastName("B");

        iona.getDatabase().getDao(Person.class).create(person);

        given().body(JsonUtil.get().toJson(person)).when().post("/person")
                .then().assertThat().statusCode(201).contentType(ContentType.JSON).body(isSamePerson(person));
    }

    @Test
    public void testPostEmptyBody() throws Exception {
        iona.add(Person.class);
        Thread.sleep(1500);

        Person person = new Person();
        person.setId(1L);
        person.setFirstName("A");
        person.setLastName("B");

        iona.getDatabase().getDao(Person.class).create(person);

        person.setFirstName(null);
        person.setLastName(null);

        given().body("{}").when().post("/person")
                .then().assertThat().statusCode(201).contentType(ContentType.JSON).body(isSamePerson(person));
    }

    @Test
    public void testPostResponseLocationHeader() throws Exception {
        iona.add(Person.class);
        Thread.sleep(1500);

        Person person = new Person();
        person.setId(1L);
        person.setFirstName("A");
        person.setLastName("B");

        iona.getDatabase().getDao(Person.class).create(person);

        given().body(JsonUtil.get().toJson(person)).when().post("/person")
                .then().assertThat().statusCode(201).contentType(ContentType.JSON).body(isSamePerson(person))
                .header(Header.Location.value(), equalTo("http://localhost:4568/person/" + (person.getId() + 1)));
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
