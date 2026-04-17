package com.user.management.api.testing.tests;

import org.assertj.core.api.SoftAssertions;
import org.testng.annotations.Test;

import com.user.management.api.testing.factory.UserFactory;
import com.user.management.api.testing.model.User;

public class CreateUserTest extends BaseTest {

    @Test
    void createUser_shouldReturn201() {

        User request = UserFactory.generateNewUser();

        var userCreationResponse = userService.createUser(request);
        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(userCreationResponse.getStatusCode()).isEqualTo(201);
            softly.assertThat(userCreationResponse.getData().getName()).isEqualTo(request.getName());
            softly.assertThat(userCreationResponse.getData().getEmail()).isEqualTo(request.getEmail());
            softly.assertThat(userCreationResponse.getData().getAge()).isEqualTo(request.getAge());
        });

        User getUserResponse = userService.getUserByEmail(request.getEmail()).getData();
        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(getUserResponse.getName()).isEqualTo(request.getName());
            softly.assertThat(getUserResponse.getEmail()).isEqualTo(request.getEmail());
            softly.assertThat(getUserResponse.getAge()).isEqualTo(request.getAge());
        });
    }

    @Test
    void createUser_shouldReturn201WithMinimumAge() {

        var response = userService.createUser(
                UserFactory.generateNewUser()
                        .toBuilder()
                        .age(1)
                        .build());

        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(response.getStatusCode()).isEqualTo(201);
            softly.assertThat(response.getData().getAge()).isEqualTo(1);
        });
    }

    @Test
    void createUser_shouldReturn201WithMaximumAge() {

        var response = userService.createUser(
                UserFactory.generateNewUser()
                        .toBuilder()
                        .age(150)
                        .build());

        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(response.getStatusCode()).isEqualTo(201);
            softly.assertThat(response.getData().getAge()).isEqualTo(150);
        });
    }

    @Test
    void createUser_shouldReturn409WhenEmailAlreadyExists() {

        User request = UserFactory.generateNewUser();
        userService.createUser(request);

        var response = userService.createUser(request);

        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(response.getStatusCode()).isEqualTo(409);
            softly.assertThat(response.isSuccess()).isFalse();
            softly.assertThat(response.getError().error()).isNotBlank();
        });
    }

    @Test
    void createUser_shouldReturn400WhenNameIsMissing() {

        var response = userService.createUser(
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
    void createUser_shouldReturn400WhenEmailIsMissing() {

        var response = userService.createUser(
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
    void createUser_shouldReturn400WhenEmailIsInvalid() {

        var response = userService.createUser(
                UserFactory.generateNewUser()
                        .toBuilder()
                        .email("ABC")
                        .build());

        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(response.getStatusCode()).isEqualTo(400);
            softly.assertThat(response.isSuccess()).isFalse();
            softly.assertThat(response.getError()).isNotNull();
            if (response.getError() != null) {
                softly.assertThat(response.getError().error()).isNotBlank();
            }
        });
    }

    @Test
    void createUser_shouldReturn400WhenAgeIsMissing() {

        var response = userService.createUser(
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
    void createUser_shouldReturn400WhenAgeBelowMinimum() {

        var response = userService.createUser(
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
    void createUser_shouldReturn400WhenAgeExceedsMaximum() {

        var response = userService.createUser(
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
