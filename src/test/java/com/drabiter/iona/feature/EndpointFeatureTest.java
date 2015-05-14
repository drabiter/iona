package com.drabiter.iona.feature;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.drabiter.iona.Iona;
import com.drabiter.iona._meta.CustomPerson;
import com.drabiter.iona._meta.Helper;
import com.drabiter.iona.exception.IonaException;
import com.jayway.restassured.RestAssured;
import com.jayway.restassured.response.ValidatableResponse;

import static com.jayway.restassured.RestAssured.*;
import static com.jayway.restassured.path.json.JsonPath.*;
import static spark.SparkBase.*;

public class EndpointFeatureTest {

    private static Iona iona;

    @BeforeClass
    public static void setup() {
        port(Helper.TEST_PORT);
    }

    @AfterClass
    public static void tearDown() {
        stop();
    }

    @Before
    public void before() throws IonaException {
        RestAssured.port = Helper.TEST_PORT;

        iona = Iona.init("jdbc:mysql://localhost:3306/iona", "root", "").add(CustomPerson.class);
    }

    @After
    public void after() {
        RestAssured.reset();
    }

    @Test
    public void testUseCustomEndpoint() {
        get("/custom_endpoint").then().assertThat().statusCode(200);

        ValidatableResponse response = given().body("{}").when().post("/custom_endpoint").then().assertThat().statusCode(201);

        int id = from(response.extract().asString()).get("id");

        given().body("{\"social_number\":3}").when().put("/custom_endpoint/" + id).then().assertThat().statusCode(200);
        delete("/custom_endpoint/" + id).then().assertThat().statusCode(204);
    }

    @Test
    public void testClearRoutes() {
        get("/custom_endpoint").then().assertThat().statusCode(200);
        get("/ping").then().assertThat().statusCode(200);

        iona.clearRoutes();

        get("/custom_endpoint").then().assertThat().statusCode(404);
        get("/ping").then().assertThat().statusCode(404);
    }

    @Test
    public void testAPIContext() {
        get("/custom_endpoint").then().assertThat().statusCode(200);

        ValidatableResponse response = given().body("{}").when().post("/custom_endpoint").then().assertThat().statusCode(201);

        int id = from(response.extract().asString()).get("id");

        given().body("{\"social_number\":3}").when().put("/custom_endpoint/" + id).then().assertThat().statusCode(200);
        delete("/custom_endpoint/" + id).then().assertThat().statusCode(204);
    }
}
