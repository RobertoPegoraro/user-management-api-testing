package com.user.management.api.testing.tests;

import org.assertj.core.api.SoftAssertions;
import org.testng.annotations.Test;

import com.user.management.api.testing.factory.UserFactory;
import com.user.management.api.testing.model.User;

public class GetUserTest extends BaseTest {

    @Test
    void getUserByEmail_shouldReturn200() {

        User request = UserFactory.generateNewUser();
        userService.createUser(request);

        var response = userService.getUserByEmail(request.getEmail());

        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(response.getStatusCode()).isEqualTo(200);
            softly.assertThat(response.getData().getName()).isEqualTo(request.getName());
            softly.assertThat(response.getData().getEmail()).isEqualTo(request.getEmail());
            softly.assertThat(response.getData().getAge()).isEqualTo(request.getAge());
        });
    }

    @Test
    void getUserByEmail_shouldReturn404WhenUserNotFound() {

        var response = userService.getUserByEmail("nonexistent@example.com");

        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(response.getStatusCode()).isEqualTo(404);
            softly.assertThat(response.isSuccess()).isFalse();
            softly.assertThat(response.getError().error()).isNotBlank();
        });
    }
}
