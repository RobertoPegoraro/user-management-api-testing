package com.user.management.api.testing.service;

import java.util.List;

import com.user.management.api.testing.core.http.model.ApiResponseWrapper;
import com.user.management.api.testing.model.User;

public interface UserService {

    ApiResponseWrapper<List<User>> listUsers();

    ApiResponseWrapper<User> createUser(User user);

    ApiResponseWrapper<User> getUserByEmail(String email);

    ApiResponseWrapper<User> updateUser(String email, User user);

    ApiResponseWrapper<Void> deleteUserByEmail(String email);
}
