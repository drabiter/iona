package com.drabiter.iona.integration;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.drabiter.iona.Iona;
import com.drabiter.iona._meta.TestUtils;
import com.drabiter.iona._meta.Person;
import com.drabiter.iona.util.JsonUtil;
import com.j256.ormlite.table.TableUtils;
import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;

import static com.drabiter.iona._meta.IsSamePerson.*;
import static com.jayway.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class GetIntegrationTest {

    private static Iona iona;

    @BeforeClass
    public static void setup() throws Exception {
        iona = TestUtils.iona().add(Person.class);
    }

    @AfterClass
    public static void tearDown() {
        iona.stop();
    }

    @Before
    public void before() throws Exception {
        TableUtils.clearTable(iona.getDatabase().getConnectionPool(), Person.class);
        RestAssured.port = TestUtils.TEST_PORT;
        Iona.clearRoutes();
    }

    @After
    public void after() throws Exception {
        reset();
    }

    @Test
    public void testGetSingle() throws Exception {
        Person person = new Person();
        person.setId(1L);
        person.setFirstName("Hongo");
        person.setLastName("Takeshi");

        iona.getDatabase().getDao(Person.class).create(person);

        iona.start();

        get("/person/" + person.getId()).then().assertThat().statusCode(200).contentType(ContentType.JSON).body(isSamePerson(person));
    }

    @Test
    public void testGetMany() throws Exception {
        Person person1 = new Person();
        person1.setId(1L);
        person1.setFirstName("Hongo");
        person1.setLastName("Takeshi");

        Person person2 = new Person();
        person2.setId(2L);
        person2.setFirstName("Ichimonji");
        person2.setLastName("Hayato");

        iona.getDatabase().getDao(Person.class).create(person1);
        iona.getDatabase().getDao(Person.class).create(person2);

        iona.start();

        String jsons = JsonUtil.get().toJson(new Person[] { person1, person2 });

        get("/person").then().assertThat().statusCode(200).contentType(ContentType.JSON).body(equalToIgnoringCase(jsons));
    }

    @Test
    public void testGetNotExist() throws Exception {
        iona.start();

        get("/person/1").then().assertThat().statusCode(404).contentType(ContentType.HTML).body(TestUtils.MATCHER_HTML_404);
    }

}
