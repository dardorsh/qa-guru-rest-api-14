package tests;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.with;

public class TestBase {

    public static final String API_KEY = "reqres-free-v1";

    static {
        RequestSpecification baseSpec = new RequestSpecBuilder()
                .setBaseUri("https://reqres.in")
                .addHeader("x-api-key", API_KEY)
                .build();

        RestAssured.requestSpecification = baseSpec;
    }
}
