package com.naman.framework.utils;

import com.naman.framework.config.ConfigManager;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

import java.util.Map;

/**
 * Fluent REST-Assured request builder with auth, headers, and logging pre-configured.
 */
public class RequestBuilder {
    private final RequestSpecBuilder specBuilder;
    private static final ConfigManager config = ConfigManager.getInstance();

    public RequestBuilder() {
        specBuilder = new RequestSpecBuilder()
            .setBaseUri(config.getBaseUrl())
            .setContentType(ContentType.JSON)
            .addHeader("Accept", "application/json")
            .log(LogDetail.ALL);
    }

    public RequestBuilder withBearerToken() {
        specBuilder.addHeader("Authorization", "Bearer " + config.getAuthToken());
        return this;
    }

    public RequestBuilder withHeaders(Map<String, String> headers) {
        headers.forEach(specBuilder::addHeader);
        return this;
    }

    public RequestBuilder withQueryParam(String key, Object value) {
        specBuilder.addQueryParam(key, value);
        return this;
    }

    public RequestBuilder withBody(Object body) {
        specBuilder.setBody(body);
        return this;
    }

    public RequestSpecification build() {
        return specBuilder.build();
    }
}
