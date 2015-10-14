package com.drabiter.iona.feature;

import java.sql.SQLException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import spark.SparkBase;

import com.drabiter.iona.Iona;
import com.drabiter.iona._meta.CustomPerson;
import com.drabiter.iona._meta.TestUtils;
import com.drabiter.iona.exception.IonaException;
import com.j256.ormlite.table.TableUtils;
import com.jayway.restassured.RestAssured;
import com.jayway.restassured.response.ValidatableResponse;

import static com.jayway.restassured.RestAssured.*;
import static com.jayway.restassured.path.json.JsonPath.*;

public class EndpointFeatureTest {

    private Iona iona;

    @Before
    public void before() throws IonaException {
        RestAssured.port = TestUtils.TEST_PORT;

        iona = TestUtils.iona().add(CustomPerson.class);
        iona.start();
    }

    @After
    public void after() throws SQLException {
        TableUtils.dropTable(iona.getDatabase().getConnectionPool(), CustomPerson.class, false);
        SparkBase.stop();

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

        Iona.clearRoutes();

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
