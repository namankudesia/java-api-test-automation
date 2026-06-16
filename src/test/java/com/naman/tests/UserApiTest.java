package com.naman.tests;

import com.github.javafaker.Faker;
import com.naman.framework.models.UserRequest;
import com.naman.framework.utils.RequestBuilder;
import com.naman.framework.utils.ResponseValidator;
import io.restassured.response.Response;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

/**
 * End-to-end CRUD tests for the User API.
 * Tests: create, read, update, delete, negative cases, performance.
 */
public class UserApiTest {
    private static final Faker faker = new Faker();
    private String createdUserId;
    private UserRequest testUser;

    @BeforeClass
    public void setup() {
        testUser = UserRequest.builder()
            .name(faker.name().fullName())
            .email(faker.internet().emailAddress())
            .password("Test@" + faker.number().digits(6))
            .role("user")
            .build();
    }

    @Test(description = "Create user - should return 201 with user ID")
    public void testCreateUser() {
        Response response = given()
            .spec(new RequestBuilder().withBearerToken().withBody(testUser).build())
            .when().post("/users")
            .then().extract().response();

        ResponseValidator.of(response)
            .statusCode(201)
            .fieldNotNull("id")
            .fieldEquals("email", testUser.getEmail())
            .responseTimeLessThan(2000);

        createdUserId = response.jsonPath().getString("id");
    }

    @Test(description = "Get user by ID - should return 200", dependsOnMethods = "testCreateUser")
    public void testGetUserById() {
        given()
            .spec(new RequestBuilder().withBearerToken().build())
            .when().get("/users/" + createdUserId)
            .then().statusCode(200).extract().response();
    }

    @Test(description = "Update user - should return 200 with updated fields", dependsOnMethods = "testGetUserById")
    public void testUpdateUser() {
        UserRequest update = UserRequest.builder().name("Updated Name").build();
        Response response = given()
            .spec(new RequestBuilder().withBearerToken().withBody(update).build())
            .when().put("/users/" + createdUserId)
            .then().extract().response();

        ResponseValidator.of(response)
            .statusCode(200)
            .fieldEquals("name", "Updated Name");
    }

    @Test(description = "Delete user - should return 204", dependsOnMethods = "testUpdateUser")
    public void testDeleteUser() {
        given()
            .spec(new RequestBuilder().withBearerToken().build())
            .when().delete("/users/" + createdUserId)
            .then().statusCode(204);
    }

    @Test(description = "Get non-existent user - should return 404")
    public void testGetNonExistentUser() {
        given()
            .spec(new RequestBuilder().withBearerToken().build())
            .when().get("/users/99999999")
            .then().statusCode(404);
    }

    @Test(description = "Create user with invalid email - should return 400")
    public void testCreateUserInvalidEmail() {
        UserRequest invalid = UserRequest.builder()
            .name("Test").email("not-an-email").password("pass").build();
        given()
            .spec(new RequestBuilder().withBearerToken().withBody(invalid).build())
            .when().post("/users")
            .then().statusCode(400);
    }
}
