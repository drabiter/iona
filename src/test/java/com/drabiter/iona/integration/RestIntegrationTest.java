package com.drabiter.iona.integration;

import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.drabiter.iona.Iona;
import com.drabiter.iona._meta.Helper;
import com.drabiter.iona._meta.Person;
import com.drabiter.iona.util.JsonUtil;
import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.ValidatableResponse;

import static com.drabiter.iona._meta.PersonAssert.*;
import static com.jayway.restassured.RestAssured.*;
import static org.assertj.core.api.Assertions.*;
import static org.hamcrest.Matchers.*;
import static spark.SparkBase.*;

public class RestIntegrationTest {

    private Person person;

    @BeforeClass
    public static void setup() throws Exception {
        Iona.init("jdbc:mysql://localhost:3306/iona", "root", "").port(Helper.TEST_PORT).add(Person.class);

        Thread.sleep(1500);
    }

    @AfterClass
    public static void tearDown() {
        stop();
    }

    @Before
    public void before() {
        RestAssured.port = Helper.TEST_PORT;

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

        Person update = new Person();
        update.setFirstName("X");

        ValidatableResponse putResponse = given().body(JsonUtil.get().toJson(update)).when().put("/person/" + returned.getId())
                .then().assertThat().statusCode(200).contentType(ContentType.JSON);

        Person put = JsonUtil.get().fromJson(putResponse.extract().body().asString(), Person.class);

        assertThat(put).isNotNull().hasId(returned.getId()).hasFirstName("X").hasLastName(null);

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
