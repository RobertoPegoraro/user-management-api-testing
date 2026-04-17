package com.user.management.api.testing.core.http.model;

import lombok.Getter;

@Getter
public class ApiResponseWrapper<T> {

    private T data;

    private ErrorResponse error;

    private int statusCode;

    public static <T> ApiResponseWrapper<T> success(T data, int statusCode) {

        ApiResponseWrapper<T> r = new ApiResponseWrapper<>();
        r.data = data;
        r.statusCode = statusCode;
        return r;
    }

    public static <T> ApiResponseWrapper<T> error(ErrorResponse error, int statusCode) {

        ApiResponseWrapper<T> r = new ApiResponseWrapper<>();
        r.error = error;
        r.statusCode = statusCode;
        return r;
    }

    public boolean isSuccess() {

        return error == null;
    }
}
