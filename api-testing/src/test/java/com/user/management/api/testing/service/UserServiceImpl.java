package com.user.management.api.testing.service;

import java.util.List;

import com.user.management.api.testing.core.http.client.HttpClient;
import com.user.management.api.testing.core.http.client.factory.HttpClientFactory;
import com.user.management.api.testing.core.http.model.ApiResponseWrapper;
import com.user.management.api.testing.model.User;

public class UserServiceImpl implements UserService {

    private final HttpClient httpClient;

    public UserServiceImpl() {

        this.httpClient = HttpClientFactory.getClient();
    }

    public UserServiceImpl(HttpClient httpClient) {

        this.httpClient = httpClient;
    }

    @Override
    public ApiResponseWrapper<List<User>> listUsers() {

        return httpClient.getList(UserEndpoint.USERS, User.class);
    }

    @Override
    public ApiResponseWrapper<User> createUser(User user) {

        return httpClient.post(UserEndpoint.USERS, user, User.class);
    }

    @Override
    public ApiResponseWrapper<User> getUserByEmail(String email) {

        return httpClient.get(UserEndpoint.USERS + "/" + email, User.class);
    }

    @Override
    public ApiResponseWrapper<User> updateUser(String email, User user) {

        return httpClient.put(UserEndpoint.USERS + "/" + email, user, User.class);
    }

    @Override
    public ApiResponseWrapper<Void> deleteUserByEmail(String email) {

        return httpClient.delete(UserEndpoint.USERS + "/" + email);
    }
}
