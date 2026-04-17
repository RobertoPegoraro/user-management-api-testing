package com.user.management.api.testing.factory;

import com.user.management.api.testing.model.User;
import com.user.management.api.testing.utils.FakerUtils;

public class UserFactory {

    private UserFactory() {}

    public static User generateNewUser() {

        return User.builder()
                .name(FakerUtils.generateName())
                .email(FakerUtils.generateEmail())
                .age(FakerUtils.generateAge())
                .build();
    }
}
