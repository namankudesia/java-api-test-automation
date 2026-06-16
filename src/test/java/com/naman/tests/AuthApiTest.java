package com.naman.tests;

import com.naman.framework.utils.RequestBuilder;
import com.naman.framework.utils.ResponseValidator;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import java.util.Map;

import static io.restassured.RestAssured.given;

/**
 * Authentication API tests: login, token validation, refresh, logout.
 */
public class AuthApiTest {

    @Test(description = "Login with valid credentials - should return JWT token")
    public void testLoginSuccess() {
        Map<String, String> creds = Map.of("email", "admin@test.com", "password", "Admin@123");
        Response response = given()
            .spec(new RequestBuilder().withBody(creds).build())
            .when().post("/auth/login")
            .then().extract().response();

        ResponseValidator.of(response)
            .statusCode(200)
            .fieldNotNull("accessToken")
            .fieldNotNull("refreshToken")
            .responseTimeLessThan(3000);
    }

    @Test(description = "Login with wrong password - should return 401")
    public void testLoginWrongPassword() {
        Map<String, String> creds = Map.of("email", "admin@test.com", "password", "wrongpassword");
        given()
            .spec(new RequestBuilder().withBody(creds).build())
            .when().post("/auth/login")
            .then().statusCode(401);
    }

    @Test(description = "Access protected endpoint without token - should return 401")
    public void testProtectedEndpointNoToken() {
        given()
            .spec(new RequestBuilder().build())
            .when().get("/users/me")
            .then().statusCode(401);
    }
}
