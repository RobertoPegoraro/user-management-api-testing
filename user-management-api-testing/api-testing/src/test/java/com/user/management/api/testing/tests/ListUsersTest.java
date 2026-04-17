package com.user.management.api.testing.tests;

import org.assertj.core.api.SoftAssertions;
import org.testng.annotations.Test;

import com.user.management.api.testing.factory.UserFactory;
import com.user.management.api.testing.model.User;

public class ListUsersTest extends BaseTest {

    @Test
    void listUsers_shouldContainCreatedUser() {

        User request = UserFactory.generateNewUser();
        userService.createUser(request);

        var response = userService.listUsers();

        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(response.getStatusCode()).isEqualTo(200);
            softly.assertThat(response.getData()).extracting(User::getEmail).contains(request.getEmail());
        });
    }
}
