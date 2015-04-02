package com.drabiter.iona.integration;

import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.drabiter.iona.Iona;
import com.drabiter.iona.Person;
import com.drabiter.iona.db.DatabaseProperty;
import com.drabiter.iona.utils.JsonUtil;
import com.jayway.restassured.RestAssured;
import com.jayway.restassured.response.ValidatableResponse;

import static com.jayway.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

import static org.junit.Assert.*;

import static spark.SparkBase.port;
import static spark.SparkBase.stop;

public class RestIntegrationTest {

    protected static final int TEST_PORT = 4568;

    private Person person;

    @BeforeClass
    public static void setup() throws Exception {
        port(TEST_PORT);

        DatabaseProperty dbProperty = new DatabaseProperty("com.mysql.jdbc.jdbc2.optional.MysqlDataSource", "localhost", "iona", "root", "");

        Iona.init().db(dbProperty).addModel(Person.class);
    }

    @AfterClass
    public static void tearDown() {
        stop();
    }

    @Before
    public void before() {
        RestAssured.port = TEST_PORT;

        person = new Person();
        person.setFirstName("A");
        person.setLastName("B");
    }

    @After
    public void after() {
        reset();
    }

    @Test
    public void testCreateByPost() {
        Person returned = createByPost(person);

        assertNotNull(returned);
        assertNotNull(returned.getId());
        assertEquals(person.getFirstName(), returned.getFirstName());
        assertEquals(person.getLastName(), returned.getLastName());
    }

    @Test
    public void testReadByGetOnId() {
        Person returned = createByPost(person);

        Person read = readByGetOnId(returned.getId());

        assertNotNull(read);
        assertNotNull(read.getId());
        assertEquals(person.getFirstName(), read.getFirstName());
        assertEquals(person.getLastName(), read.getLastName());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testReadByGet() {
        Person firstPerson = createByPost(person);

        person.setFirstName("C");
        person.setLastName("D");
        Person secondPerson = createByPost(person);

        ValidatableResponse getResponse = get("/person")
                .then().assertThat().statusCode(200).body(notNullValue());

        String body = getResponse.extract().body().asString();

        assertTrue(body.contains(JsonUtil.get().toJson(firstPerson)));
        assertTrue(body.contains(JsonUtil.get().toJson(secondPerson)));

        List<Person> results = JsonUtil.get().fromJson(body, List.class);

        assertTrue(results.size() >= 2);
    }

    @Test
    public void testDeleteByDelete() {
        Person returned = createByPost(person);

        readByGetOnId(returned.getId());

        delete("/person/" + returned.getId()).then().assertThat().statusCode(200).body(is(String.valueOf(returned.getId())));

        get("/person/" + returned.getId()).then().assertThat().statusCode(404);
    }

    @Test
    public void testUpdateByPut() {
        Person returned = createByPost(person);

        assertNotEquals("X", returned.getFirstName());
        assertNotEquals("Y", returned.getLastName());

        returned.setFirstName("X");
        returned.setLastName("Y");

        ValidatableResponse putResponse = given().body(JsonUtil.get().toJson(returned)).when().put("/person/" + returned.getId())
                .then().assertThat().statusCode(200);

        Person put = JsonUtil.get().fromJson(putResponse.extract().body().asString(), Person.class);

        assertNotNull(put);
        assertEquals(returned.getId(), put.getId());
        assertEquals("X", put.getFirstName());
        assertEquals("Y", put.getLastName());

        Person read = readByGetOnId(returned.getId());

        assertNotNull(read);
        assertEquals(put.getId(), read.getId());
        assertEquals(put.getFirstName(), read.getFirstName());
        assertEquals(put.getLastName(), read.getLastName());
    }

    private Person createByPost(Person person) {
        ValidatableResponse createResponse = given().body(JsonUtil.get().toJson(person)).when().post("/person")
                .then().assertThat().statusCode(201).body(notNullValue());

        return JsonUtil.get().fromJson(createResponse.extract().body().asString(), Person.class);
    }

    private Person readByGetOnId(long id) {
        ValidatableResponse getResponse = get("/person/" + id)
                .then().assertThat().statusCode(200).body(notNullValue());

        return JsonUtil.get().fromJson(getResponse.extract().body().asString(), Person.class);
    }

}
