package com.user.management.api.testing.core.http.parser;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.user.management.api.testing.core.http.model.ErrorResponse;
import com.user.management.api.testing.core.http.model.ObjectMapperHolder;

public class ResponseParser {

    private ResponseParser() {}

    public static ErrorResponse convertError(String body) {

        try {
            return ObjectMapperHolder.INSTANCE.readValue(body, ErrorResponse.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
