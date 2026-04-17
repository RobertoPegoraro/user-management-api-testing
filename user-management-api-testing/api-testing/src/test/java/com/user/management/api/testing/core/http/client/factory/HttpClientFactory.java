package com.user.management.api.testing.core.http.client.factory;

import com.user.management.api.testing.core.http.client.HttpClient;
import com.user.management.api.testing.core.http.client.RestAssuredHttpClientImpl;

public class HttpClientFactory {

    private static final HttpClient instance = new RestAssuredHttpClientImpl();

    private HttpClientFactory() {}

    public static HttpClient getClient() {

        return instance;
    }
}
