package com.user.management.api.testing.core.http.model;

import com.fasterxml.jackson.databind.ObjectMapper;

public class ObjectMapperHolder {

    public static final ObjectMapper INSTANCE = new ObjectMapper();

    private ObjectMapperHolder() {}
}
