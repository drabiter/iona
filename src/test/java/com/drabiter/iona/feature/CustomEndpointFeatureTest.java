package com.drabiter.iona.feature;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.drabiter.iona.Iona;
import com.drabiter.iona._meta.CustomPerson;
import com.jayway.restassured.RestAssured;
import com.jayway.restassured.response.ValidatableResponse;

import static com.jayway.restassured.RestAssured.*;
import static com.jayway.restassured.path.json.JsonPath.*;
import static spark.SparkBase.*;

public class CustomEndpointFeatureTest {

    protected static final int TEST_PORT = 4568;

    @BeforeClass
    public static void setup() throws Exception {
        port(TEST_PORT);

        Iona.init().mysql("localhost", 3306, "iona", "root", "").add(CustomPerson.class);
    }

    @Before
    public void before() {
        RestAssured.port = TEST_PORT;
    }

    @After
    public void after() {
        RestAssured.reset();
    }

    @AfterClass
    public static void tearDown() {
        stop();
    }

    @Test
    public void testUseCustomEndpoint() {
        get("/custom_endpoint").then().assertThat().statusCode(200);

        ValidatableResponse response = given().body("{}").when().post("/custom_endpoint").then().assertThat().statusCode(201);

        int id = from(response.extract().asString()).get("id");

        given().body("{\"social_number\":3}").when().put("/custom_endpoint/" + id).then().assertThat().statusCode(200);
        delete("/custom_endpoint/" + id).then().assertThat().statusCode(202);
    }
}
