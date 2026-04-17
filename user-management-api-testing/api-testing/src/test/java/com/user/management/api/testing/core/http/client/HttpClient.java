package com.user.management.api.testing.core.http.client;

import java.util.List;

import com.user.management.api.testing.core.http.model.ApiResponseWrapper;

public interface HttpClient {

    HttpClient withToken(String token);

    <T> ApiResponseWrapper<List<T>> getList(String endpoint, Class<T> response);

    <T> ApiResponseWrapper<T> get(String endpoint, Class<T> response);

    <T> ApiResponseWrapper<T> post(String endpoint, Object body, Class<T> response);

    <T> ApiResponseWrapper<T> put(String endpoint, Object body, Class<T> response);

    ApiResponseWrapper<Void> delete(String endpoint);
}
