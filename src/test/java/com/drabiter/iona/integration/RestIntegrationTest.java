package com.drabiter.iona.integration;

import java.sql.SQLException;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import spark.SparkBase;

import com.drabiter.iona.Iona;
import com.drabiter.iona._meta.Person;
import com.drabiter.iona._meta.TestUtils;
import com.drabiter.iona.util.JsonUtil;
import com.j256.ormlite.table.TableUtils;
import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.ValidatableResponse;

import static com.drabiter.iona._meta.IsSamePerson.*;
import static com.drabiter.iona._meta.PersonAssert.*;
import static com.jayway.restassured.RestAssured.*;
import static org.assertj.core.api.Assertions.*;
import static org.hamcrest.Matchers.*;

public class RestIntegrationTest {

    private static Iona iona;

    private Person person;

    @BeforeClass
    public static void setup() throws Exception {
        iona = TestUtils.iona().add(Person.class);
        iona.start();
    }

    @AfterClass
    public static void tearDown() throws SQLException {
        TableUtils.dropTable(iona.getDatabase().getConnectionPool(), Person.class, false);
        SparkBase.stop();
    }

    @Before
    public void before() {
        RestAssured.port = TestUtils.TEST_PORT;

        person = new Person();
        person.setFirstName("A");
        person.setLastName("B");
    }

    @After
    public void after() {
        reset();
    }

    @Test
    public void testPing() {
        get("/ping").then().assertThat().statusCode(200).body(equalTo("pong"));
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

        delete("/person/" + returned.getId()).then().assertThat().statusCode(204).body(isEmptyOrNullString());

        get("/person/" + returned.getId()).then().assertThat().statusCode(404);
    }

    @Test
    public void testUpdateByPutFull() {
        Person returned = createByPost(person);

        assertThat(returned.getFirstName()).isEqualTo("A");
        assertThat(returned.getLastName()).isEqualTo("B");

        Person updated = new Person();
        updated.setFirstName("X");

        given().body(JsonUtil.get().toJson(updated)).when().put("/person/" + returned.getId())
                .then().assertThat().statusCode(200).contentType(ContentType.JSON).body(isSamePerson(updated));

        Person read = readByGetOnId(returned.getId());

        assertThat(read).isNotNull().hasFirstName(updated.getFirstName()).hasLastName(updated.getLastName());
    }

    private Person createByPost(Person person) {
        ValidatableResponse createResponse = given().body(JsonUtil.get().toJson(person)).when().post("/person")
                .then().assertThat().statusCode(201).contentType(ContentType.JSON).body(isSamePerson(person));

        return JsonUtil.get().fromJson(createResponse.extract().body().asString(), Person.class);
    }

    private Person readByGetOnId(long id) {
        ValidatableResponse getResponse = get("/person/" + id)
                .then().assertThat().statusCode(200).contentType(ContentType.JSON).body(notNullValue());

        return JsonUtil.get().fromJson(getResponse.extract().body().asString(), Person.class);
    }

}
