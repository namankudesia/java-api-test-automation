package com.naman.framework.utils;

import io.restassured.response.Response;
import org.testng.Assert;
import java.util.List;

/**
 * Fluent response validator for status codes, body fields, schemas, and timing.
 */
public class ResponseValidator {
    private final Response response;

    private ResponseValidator(Response response) {
        this.response = response;
    }

    public static ResponseValidator of(Response response) {
        return new ResponseValidator(response);
    }

    public ResponseValidator statusCode(int expected) {
        Assert.assertEquals(response.statusCode(), expected,
            "Status code mismatch. Body: " + response.body().asString());
        return this;
    }

    public ResponseValidator fieldEquals(String jsonPath, Object expected) {
        Object actual = response.jsonPath().get(jsonPath);
        Assert.assertEquals(actual, expected, "Field mismatch at: " + jsonPath);
        return this;
    }

    public ResponseValidator fieldNotNull(String jsonPath) {
        Assert.assertNotNull(response.jsonPath().get(jsonPath), "Field should not be null: " + jsonPath);
        return this;
    }

    public ResponseValidator bodyContains(String text) {
        Assert.assertTrue(response.body().asString().contains(text),
            "Body does not contain: " + text);
        return this;
    }

    public ResponseValidator responseTimeLessThan(long millis) {
        Assert.assertTrue(response.time() < millis,
            "Response time " + response.time() + "ms exceeded " + millis + "ms");
        return this;
    }

    public ResponseValidator listNotEmpty(String jsonPath) {
        List<?> list = response.jsonPath().getList(jsonPath);
        Assert.assertNotNull(list);
        Assert.assertFalse(list.isEmpty(), "List at " + jsonPath + " should not be empty");
        return this;
    }

    public Response getResponse() { return response; }
}
