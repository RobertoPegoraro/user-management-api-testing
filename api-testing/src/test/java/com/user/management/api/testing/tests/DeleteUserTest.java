package com.user.management.api.testing.tests;

import org.assertj.core.api.SoftAssertions;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.user.management.api.testing.core.config.Config;
import com.user.management.api.testing.factory.UserFactory;
import com.user.management.api.testing.model.User;

public class DeleteUserTest extends BaseTest {

    @Override
    @BeforeClass
    void setup() {

        userService = serviceWithToken(Config.get().token());
    }

    @Test
    void deleteUser_shouldReturn204() {

        User created = userService.createUser(UserFactory.generateNewUser()).getData();

        var response = userService.deleteUserByEmail(created.getEmail());

        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(response.getStatusCode()).isEqualTo(204);
            softly.assertThat(response.isSuccess()).isTrue();
        });
    }

    @Test
    void deleteUser_shouldMakeUserUnreachableAfterDeletion() {

        User created = userService.createUser(UserFactory.generateNewUser()).getData();
        userService.deleteUserByEmail(created.getEmail());

        var response = userService.getUserByEmail(created.getEmail());

        SoftAssertions.assertSoftly(softly ->
                softly.assertThat(response.getStatusCode()).isEqualTo(404)
        );
    }

    @Test
    void deleteUser_shouldReturn404OnSecondDelete() {

        User created = userService.createUser(UserFactory.generateNewUser()).getData();
        userService.deleteUserByEmail(created.getEmail());

        var response = userService.deleteUserByEmail(created.getEmail());

        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(response.getStatusCode()).isEqualTo(404);
            softly.assertThat(response.isSuccess()).isFalse();
            softly.assertThat(response.getError().error()).isNotBlank();
        });
    }

    @Test
    void deleteUser_shouldReturn404WhenUserNotFound() {

        var response = userService.deleteUserByEmail("nonexistent@example.com");

        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(response.getStatusCode()).isEqualTo(404);
            softly.assertThat(response.isSuccess()).isFalse();
            softly.assertThat(response.getError().error()).isNotBlank();
        });
    }
}
