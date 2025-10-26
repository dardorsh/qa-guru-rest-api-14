package tests;

import models.lombok.ErrorModel;
import models.lombok.RegisterResponseLombokModel;
import models.lombok.RegisterUserLombokModel;
import models.lombok.UsersResponseModel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.startsWith;
import static org.junit.jupiter.api.Assertions.*;
import static specs.RegisterSpec.*;
import static specs.UsersSpec.*;

public class RestApiTests extends TestBase {

    @Test
    @DisplayName("Проверка, что запрос /register регистрирует нового пользователя")
    void successfulRegisterUserTest() {
        RegisterUserLombokModel user = new RegisterUserLombokModel();
        user.setEmail("eve.holt@reqres.in");
        user.setPassword("pistol");

        RegisterResponseLombokModel response = step("Make request", () ->
                given(registerRequestSpec)
                        .body(user)
                        .when()
                        .post()
                        .then()
                        .spec(registerResponseSpec)
                        .extract().as(RegisterResponseLombokModel.class));

        step("Check response", () -> {
            assertNotNull(response.getId());
            assertEquals("QpwL5tke4Pnpja7X4", response.getToken());
        });
    }

    @Test
    @DisplayName("Проверка, что запрос /register c неверными авторизационными данными вернет ошибку")
    void wrongRegisterDataTest() {
        RegisterUserLombokModel user = new RegisterUserLombokModel();
        user.setEmail("some-wrong-email@test.com");
        user.setPassword("pistol");

        ErrorModel response = step("Make request", () ->
                given(registerRequestSpec)
                        .body(user)
                        .when()
                        .post()
                        .then()
                        .spec(wrongCredsSpec)
                        .extract().as(ErrorModel.class));

        step("Check response", () -> {
            assertEquals("Note: Only defined users succeed registration", response.getError());
        });

    }

    @Test
    @DisplayName("Проверка, что GET-запрос /users с query params отдает нужное количество users")
    void successfulGetUsersListWithQueryParamsTest() {
        UsersResponseModel response = step("Make request", () ->
                given()
                        .spec(getUsersRequestSpec("1", "12"))
                        .when()
                        .get()
                        .then()
                        .spec(getUsersResponseSpec)
                        .extract().as(UsersResponseModel.class));
        step("Check response", () -> {
            assertEquals(response.getPer_page(), response.getTotal());
        });
    }

    @Test
    @DisplayName("Проверка, что GET-запрос /users/{id} не вернет данные без правильного авторизационного токена")
    void failedGetUserDataWithoutApiKeyTest() {
        ErrorModel response = step("Make request", () ->
                given()
                        .spec(getUserRequestSpec("2"))
                        .header("x-api-key", "empty")
                        .when()
                        .get()
                        .then()
                        .spec(getUserWithoutTokenResponseSpec)
                        .extract().as(ErrorModel.class));

        step("Check response", () ->
                assertEquals("Missing API key", response.getError()));
    }

    @Test
    @DisplayName("Проверка, что DELETE-запрос /users/{id} удалит пользователя")
    void successfulDeleteUserTest() {
        given()
                .spec(getUserRequestSpec("1"))
                .when()
                .delete()
                .then()
                .spec(deleteUserResponseSpec);
    }

    @Test
    @DisplayName("Проверка, что PATCH-запрос /users/{id} обновит пользователя")
    void successfulPatchUserTest() {
        given()
                .spec(patchUserRequestSpec("1", "some-new-email@test.com"))
                .when()
                .patch()
                .then()
                .spec(patchUserResponseSpec)
                .body("updatedAt", startsWith("2025"));
    }
}
