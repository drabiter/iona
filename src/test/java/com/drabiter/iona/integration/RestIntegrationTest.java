package com.drabiter.iona.integration;

import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.drabiter.iona.Iona;
import com.drabiter.iona._meta.Person;
import com.drabiter.iona.db.DatabaseProperty;
import com.drabiter.iona.utils.JsonUtil;
import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.ValidatableResponse;

import static com.drabiter.iona._meta.PersonAssert.*;
import static com.jayway.restassured.RestAssured.*;
import static org.assertj.core.api.Assertions.*;
import static org.hamcrest.Matchers.*;
import static spark.SparkBase.*;

public class RestIntegrationTest {

    protected static final int TEST_PORT = 4568;

    private Person person;

    @BeforeClass
    public static void setup() throws Exception {
        port(TEST_PORT);

        DatabaseProperty dbProperty = new DatabaseProperty(
                "com.mysql.jdbc.jdbc2.optional.MysqlDataSource",
                "localhost",
                "iona",
                "root",
                "");

        Iona.init().db(dbProperty).add(Person.class);
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

        assertThat(returned).isNotNull().hasId().hasFirstName(person.getFirstName()).hasLastName(person.getLastName());
    }

    @Test
    public void testReadByGetOnId() {
        Person returned = createByPost(person);

        Person read = readByGetOnId(returned.getId());

        assertThat(read).isNotNull().hasId().hasFirstName(person.getFirstName()).hasLastName(person.getLastName());
    }

    @Test
    public void testReadByGet() {
        Person firstPerson = createByPost(person);

        person.setFirstName("C");
        person.setLastName("D");
        Person secondPerson = createByPost(person);

        ValidatableResponse getResponse = get("/person")
                .then().assertThat().statusCode(200).contentType(ContentType.JSON).body(notNullValue());

        String body = getResponse.extract().body().asString();

        assertThat(body).contains(JsonUtil.get().toJson(firstPerson)).contains(JsonUtil.get().toJson(secondPerson));

        List<?> results = JsonUtil.get().fromJson(body, List.class);

        assertThat(results.size()).isGreaterThanOrEqualTo(2);
    }

    @Test
    public void testDeleteByDelete() {
        Person returned = createByPost(person);

        readByGetOnId(returned.getId());

        delete("/person/" + returned.getId()).then().assertThat().statusCode(202).contentType(ContentType.TEXT).body(is(String.valueOf(returned.getId())));

        get("/person/" + returned.getId()).then().assertThat().statusCode(404);
    }

    @Test
    public void testUpdateByPut() {
        Person returned = createByPost(person);

        assertThat(returned.getFirstName()).isNotEqualTo("X");
        assertThat(returned.getLastName()).isNotEqualTo("Y");

        returned.setFirstName("X");
        returned.setLastName(null);

        ValidatableResponse putResponse = given().body(JsonUtil.get().toJson(returned)).when().put("/person/" + returned.getId())
                .then().assertThat().statusCode(200).contentType(ContentType.JSON);

        Person put = JsonUtil.get().fromJson(putResponse.extract().body().asString(), Person.class);

        assertThat(put).isNotNull().hasId(returned.getId()).hasFirstName("X").hasLastName("B");

        Person read = readByGetOnId(returned.getId());

        assertThat(read).isNotNull().hasId(put.getId()).hasFirstName(put.getFirstName()).hasLastName(put.getLastName());
    }

    private Person createByPost(Person person) {
        ValidatableResponse createResponse = given().body(JsonUtil.get().toJson(person)).when().post("/person")
                .then().assertThat().statusCode(201).contentType(ContentType.JSON).body(notNullValue());

        return JsonUtil.get().fromJson(createResponse.extract().body().asString(), Person.class);
    }

    private Person readByGetOnId(long id) {
        ValidatableResponse getResponse = get("/person/" + id)
                .then().assertThat().statusCode(200).contentType(ContentType.JSON).body(notNullValue());

        return JsonUtil.get().fromJson(getResponse.extract().body().asString(), Person.class);
    }

}
