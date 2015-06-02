package com.drabiter.iona.integration;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.drabiter.iona.Iona;
import com.drabiter.iona._meta.Helper;
import com.drabiter.iona._meta.Person;
import com.drabiter.iona.exception.IonaException;
import com.drabiter.iona.util.JsonUtil;
import com.j256.ormlite.table.TableUtils;
import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;

import static com.jayway.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class GetIntegrationTest {

    private static Iona iona;

    @BeforeClass
    public static void setup() throws Exception {
        iona = Helper.getIona();
    }

    @AfterClass
    public static void tearDown() {
        Iona.stop();
    }

    @Before
    public void before() throws IonaException {
        RestAssured.port = Helper.TEST_PORT;
        Iona.clearRoutes();
    }

    @After
    public void after() throws Exception {
        reset();
    }

    @Test
    public void testGet() throws Exception {
        TableUtils.clearTable(iona.getDatabase().getConnectionPool(), Person.class);

        Person person = new Person();
        person.setId(1L);
        person.setFirstName("Hongo");
        person.setLastName("Takeshi");

        iona.add(Person.class).start();

        get("/person/" + person.getId()).then().assertThat().statusCode(404).contentType(ContentType.HTML).body(Helper.MATCHER_HTML_404);

        iona.getDatabase().getDao(Person.class).create(person);

        String json = JsonUtil.get().toJson(person);
        String jsons = JsonUtil.get().toJson(new Person[] { person });

        get("/person/" + person.getId()).then().assertThat().statusCode(200).contentType(ContentType.JSON).body(equalToIgnoringCase(json));
        get("/person/" + (person.getId() + 999)).then().assertThat().statusCode(404).contentType(ContentType.HTML).body(Helper.MATCHER_HTML_404);
        get("/person").then().assertThat().statusCode(200).contentType(ContentType.JSON).body(equalToIgnoringCase(jsons));
    }
}
