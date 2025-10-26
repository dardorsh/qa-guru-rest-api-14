package specs;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

import static helpers.CustomAllureListener.withCustomTemplates;
import static io.restassured.RestAssured.with;
import static io.restassured.filter.log.LogDetail.BODY;
import static io.restassured.filter.log.LogDetail.STATUS;

public class UsersSpec {

    public static RequestSpecification getUsersRequestSpec(String page, String per_page) {
        return with()
                .filter(withCustomTemplates())
                .queryParam("page", page)
                .queryParam("per_page", per_page)
                .basePath("/api/users");
    }

    public static ResponseSpecification getUsersResponseSpec = new ResponseSpecBuilder()
            .expectStatusCode(200)
            .log(STATUS)
            .log(BODY)
            .build();

    public static RequestSpecification getUserRequestSpec(String user_id) {
        return with()
                .filter(withCustomTemplates())
                .basePath("/api/users/" + user_id);
    }

    public static ResponseSpecification getUserWithoutTokenResponseSpec = new ResponseSpecBuilder()
            .expectStatusCode(401)
            .log(STATUS)
            .log(BODY)
            .build();

    public static ResponseSpecification deleteUserResponseSpec = new ResponseSpecBuilder()
            .expectStatusCode(204)
            .log(STATUS)
            .log(BODY)
            .build();

    public static RequestSpecification patchUserRequestSpec(String user_id, String email) {
        return with()
                .filter(withCustomTemplates())
                .param("email", email)
                .basePath("/api/users/" + user_id);
    }

    public static ResponseSpecification patchUserResponseSpec = new ResponseSpecBuilder()
            .expectStatusCode(200)
            .log(STATUS)
            .log(BODY)
            .build();
}
