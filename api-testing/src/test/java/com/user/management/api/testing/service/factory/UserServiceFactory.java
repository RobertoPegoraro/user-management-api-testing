package com.user.management.api.testing.service.factory;

import com.user.management.api.testing.core.http.client.factory.HttpClientFactory;
import com.user.management.api.testing.service.UserService;
import com.user.management.api.testing.service.UserServiceImpl;

public class UserServiceFactory {

    private static final UserService instance = new UserServiceImpl();

    private UserServiceFactory() {}

    public static UserService getService() {

        return instance;
    }

    public static UserService getServiceWithToken(String token) {

        return new UserServiceImpl(HttpClientFactory.getClient().withToken(token));
    }
}
