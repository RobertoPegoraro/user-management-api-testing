package com.user.management.api.testing.core.http.client;

import static io.restassured.RestAssured.given;

import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.user.management.api.testing.core.config.Config;
import com.user.management.api.testing.core.http.header.HeaderUtils;
import com.user.management.api.testing.core.http.logger.HttpLogger;
import com.user.management.api.testing.core.http.model.ApiResponseWrapper;
import com.user.management.api.testing.core.http.model.ObjectMapperHolder;
import com.user.management.api.testing.core.http.parser.ResponseParser;

import io.restassured.http.Header;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class RestAssuredHttpClientImpl implements HttpClient {

    private final String baseUrl;

    private final String token;

    public RestAssuredHttpClientImpl() {

        this.baseUrl = Config.get().baseUrl();
        this.token = null;
    }

    private RestAssuredHttpClientImpl(String token) {

        this.baseUrl = Config.get().baseUrl();
        this.token = token;
    }

    @Override
    public HttpClient withToken(String token) {

        return new RestAssuredHttpClientImpl(token);
    }

    @Override
    public <T> ApiResponseWrapper<List<T>> getList(String endpoint, Class<T> responseType) {

        String url = baseUrl + endpoint;
        Response response = baseSpec().get(url);
        HttpLogger.logExchange("GET", url, null, response.getStatusCode(), response.getBody().asString());
        if (response.getStatusCode() >= 400) {
            return ApiResponseWrapper.error(ResponseParser.convertError(response.getBody().asString()),
                    response.getStatusCode());
        }
        return ApiResponseWrapper.success(response.jsonPath().getList(".", responseType), response.getStatusCode());
    }

    @Override
    public <T> ApiResponseWrapper<T> get(String endpoint, Class<T> responseType) {

        String url = baseUrl + endpoint;
        Response response = baseSpec().get(url);
        HttpLogger.logExchange("GET", url, null, response.getStatusCode(), response.getBody().asString());
        return wrapResponse(response, responseType);
    }

    @Override
    public <T> ApiResponseWrapper<T> post(String endpoint, Object requestObject, Class<T> responseType) {

        String url = baseUrl + endpoint;
        String body = toJson(requestObject);
        Response response = baseSpec().body(body).post(url);
        HttpLogger.logExchange("POST", url, body, response.getStatusCode(), response.getBody().asString());
        return wrapResponse(response, responseType);
    }

    @Override
    public <T> ApiResponseWrapper<T> put(String endpoint, Object requestObject, Class<T> responseType) {

        String url = baseUrl + endpoint;
        String body = toJson(requestObject);
        Response response = baseSpec().body(body).put(url);
        HttpLogger.logExchange("PUT", url, body, response.getStatusCode(), response.getBody().asString());
        return wrapResponse(response, responseType);
    }

    @Override
    public ApiResponseWrapper<Void> delete(String endpoint) {

        String url = baseUrl + endpoint;
        Response response = baseSpec().delete(url);
        HttpLogger.logExchange("DELETE", url, null, response.getStatusCode(), response.getBody().asString());
        return wrapVoidResponse(response);
    }

    private RequestSpecification baseSpec() {

        return baseSpec(this.token);
    }

    private RequestSpecification baseSpec(String token) {

        RequestSpecification spec = given().headers(HeaderUtils.getDefaultHeaders());
        if (token != null && !token.isEmpty()) {
            spec = spec.header(new Header("Authentication", token));
        }
        return spec;
    }

    private <T> ApiResponseWrapper<T> wrapResponse(Response response, Class<T> responseType) {

        if (response.getStatusCode() >= 400) {
            return ApiResponseWrapper.error(ResponseParser.convertError(response.getBody().asString()),
                    response.getStatusCode());
        }
        return ApiResponseWrapper.success(response.getBody().as(responseType), response.getStatusCode());
    }

    private ApiResponseWrapper<Void> wrapVoidResponse(Response response) {

        if (response.getStatusCode() >= 400) {
            return ApiResponseWrapper.error(ResponseParser.convertError(response.getBody().asString()),
                    response.getStatusCode());
        }
        return ApiResponseWrapper.success(null, response.getStatusCode());
    }

    private String toJson(Object obj) {

        try {
            return ObjectMapperHolder.INSTANCE.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize request body", e);
        }
    }
}
