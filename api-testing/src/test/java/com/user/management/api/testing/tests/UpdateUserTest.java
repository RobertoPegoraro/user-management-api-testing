package com.user.management.api.testing.tests;

import org.assertj.core.api.SoftAssertions;
import org.testng.annotations.Test;

import com.user.management.api.testing.factory.UserFactory;
import com.user.management.api.testing.model.User;

public class UpdateUserTest extends BaseTest {

    @Test
    void updateUser_shouldReturn200WithUpdatedData() {

        User created = userService.createUser(UserFactory.generateNewUser()).getData();
        User updateRequest = UserFactory.generateNewUser()
                .toBuilder()
                .email(created.getEmail())
                .build();

        var response = userService.updateUser(created.getEmail(), updateRequest);

        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(response.getStatusCode()).isEqualTo(200);
            softly.assertThat(response.getData().getName()).isEqualTo(updateRequest.getName());
            softly.assertThat(response.getData().getEmail()).isEqualTo(created.getEmail());
            softly.assertThat(response.getData().getAge()).isEqualTo(updateRequest.getAge());
        });

        User persisted = userService.getUserByEmail(created.getEmail()).getData();
        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(persisted.getName()).isEqualTo(updateRequest.getName());
            softly.assertThat(persisted.getEmail()).isEqualTo(created.getEmail());
            softly.assertThat(persisted.getAge()).isEqualTo(updateRequest.getAge());
        });
    }

    @Test
    void updateUser_shouldKeepEmailUnchanged() {

        User created = userService.createUser(UserFactory.generateNewUser()).getData();
        User updateRequest = UserFactory.generateNewUser();

        userService.updateUser(created.getEmail(), updateRequest);

        var response = userService.getUserByEmail(created.getEmail());

        SoftAssertions.assertSoftly(softly ->
                softly.assertThat(response.getStatusCode()).isEqualTo(200)
        );
    }

    @Test
    void updateUser_shouldReturn404WhenUserNotFound() {

        var response = userService.updateUser(
                "nonexistent@example.com", UserFactory.generateNewUser());

        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(response.getStatusCode()).isEqualTo(404);
            softly.assertThat(response.isSuccess()).isFalse();
            softly.assertThat(response.getError().error()).isNotBlank();
        });
    }

    @Test
    void updateUser_shouldReturn409WhenEmailAlreadyExists() {

        User firstUser = userService.createUser(UserFactory.generateNewUser()).getData();
        User secondUser = userService.createUser(UserFactory.generateNewUser()).getData();

        var response = userService.updateUser(
                secondUser.getEmail(),
                UserFactory.generateNewUser()
                        .toBuilder()
                        .email(firstUser.getEmail())
                        .build());

        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(response.getStatusCode()).isEqualTo(409);
            softly.assertThat(response.isSuccess()).isFalse();
            softly.assertThat(response.getError().error()).isNotBlank();
        });
    }

    @Test
    void updateUser_shouldReturn400WhenNameIsMissing() {

        User created = userService.createUser(UserFactory.generateNewUser()).getData();

        var response = userService.updateUser(
                created.getEmail(),
                UserFactory.generateNewUser()
                        .toBuilder()
                        .name(null)
                        .build());

        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(response.getStatusCode()).isEqualTo(400);
            softly.assertThat(response.isSuccess()).isFalse();
            softly.assertThat(response.getError().error()).isNotBlank();
        });
    }

    @Test
    void updateUser_shouldReturn400WhenEmailIsMissing() {

        User created = userService.createUser(UserFactory.generateNewUser()).getData();

        var response = userService.updateUser(
                created.getEmail(),
                UserFactory.generateNewUser()
                        .toBuilder()
                        .email(null)
                        .build());

        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(response.getStatusCode()).isEqualTo(400);
            softly.assertThat(response.isSuccess()).isFalse();
            softly.assertThat(response.getError().error()).isNotBlank();
        });
    }

    @Test
    void updateUser_shouldReturn400WhenEmailIsInvalid() {

        User created = userService.createUser(UserFactory.generateNewUser()).getData();

        var response = userService.updateUser(
                created.getEmail(),
                UserFactory.generateNewUser()
                        .toBuilder()
                        .email("ABC")
                        .build());

        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(response.getStatusCode()).isEqualTo(400);
            softly.assertThat(response.isSuccess()).isFalse();
            softly.assertThat(response.getError().error()).isNotBlank();
        });
    }

    @Test
    void updateUser_shouldReturn400WhenAgeIsMissing() {

        User created = userService.createUser(UserFactory.generateNewUser()).getData();

        var response = userService.updateUser(
                created.getEmail(),
                UserFactory.generateNewUser()
                        .toBuilder()
                        .age(null)
                        .build());

        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(response.getStatusCode()).isEqualTo(400);
            softly.assertThat(response.isSuccess()).isFalse();
            softly.assertThat(response.getError().error()).isNotBlank();
        });
    }

    @Test
    void updateUser_shouldReturn400WhenAgeBelowMinimum() {

        User created = userService.createUser(UserFactory.generateNewUser()).getData();

        var response = userService.updateUser(
                created.getEmail(),
                UserFactory.generateNewUser()
                        .toBuilder()
                        .age(0)
                        .build());

        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(response.getStatusCode()).isEqualTo(400);
            softly.assertThat(response.isSuccess()).isFalse();
            softly.assertThat(response.getError().error()).isNotBlank();
        });
    }

    @Test
    void updateUser_shouldReturn400WhenAgeExceedsMaximum() {

        User created = userService.createUser(UserFactory.generateNewUser()).getData();

        var response = userService.updateUser(
                created.getEmail(),
                UserFactory.generateNewUser()
                        .toBuilder()
                        .age(151)
                        .build());

        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(response.getStatusCode()).isEqualTo(400);
            softly.assertThat(response.isSuccess()).isFalse();
            softly.assertThat(response.getError().error()).isNotBlank();
        });
    }
}
