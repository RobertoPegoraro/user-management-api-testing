package com.user.management.api.testing.core.http.header;

import io.restassured.http.Header;
import io.restassured.http.Headers;

public class HeaderUtils {

    public static Headers getDefaultHeaders() {

        return new Headers(
                new Header("Accept", "*/*"),
                new Header("Content-Type", "application/json"));
    }
}
