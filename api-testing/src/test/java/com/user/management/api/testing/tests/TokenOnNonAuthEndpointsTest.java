package com.user.management.api.testing.tests;

import org.assertj.core.api.SoftAssertions;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.user.management.api.testing.core.config.Config;
import com.user.management.api.testing.factory.UserFactory;
import com.user.management.api.testing.model.User;

public class TokenOnNonAuthEndpointsTest extends BaseTest {

    @Override
    @BeforeClass
    protected void setup() {

        userService = serviceWithToken(Config.get().token());
    }

    @Test
    void listUsers_shouldReturn200WhenTokenIsSent() {

        var response = userService.listUsers();

        SoftAssertions.assertSoftly(softly ->
                softly.assertThat(response.getStatusCode()).isEqualTo(200)
        );
    }

    @Test
    void createUser_shouldReturn201WhenTokenIsSent() {

        User request = UserFactory.generateNewUser();

        var response = userService.createUser(request);

        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(response.getStatusCode()).isEqualTo(201);
            softly.assertThat(response.getData().getEmail()).isEqualTo(request.getEmail());
        });
    }

    @Test
    void getUserByEmail_shouldReturn200WhenTokenIsSent() {

        User created = userService.createUser(UserFactory.generateNewUser()).getData();

        var response = userService.getUserByEmail(created.getEmail());

        SoftAssertions.assertSoftly(softly ->
                softly.assertThat(response.getStatusCode()).isEqualTo(200)
        );
    }

    @Test
    void updateUser_shouldReturn200WhenTokenIsSent() {

        User created = userService.createUser(UserFactory.generateNewUser()).getData();
        User updateRequest = UserFactory.generateNewUser();

        var response = userService.updateUser(created.getEmail(), updateRequest);

        SoftAssertions.assertSoftly(softly ->
                softly.assertThat(response.getStatusCode()).isEqualTo(200)
        );
    }

    @Test
    void deleteUser_shouldReturn401WhenNoToken() {

        User created = userService.createUser(UserFactory.generateNewUser()).getData();

        var response = serviceWithNoToken().deleteUserByEmail(created.getEmail());

        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(response.getStatusCode()).isEqualTo(401);
            softly.assertThat(response.isSuccess()).isFalse();
            softly.assertThat(response.getError()).isNotNull();
            if (response.getError() != null) {
                softly.assertThat(response.getError().error()).isNotBlank();
            }
        });
    }

    @Test
    void deleteUser_shouldReturn401WhenInvalidToken() {

        User created = userService.createUser(UserFactory.generateNewUser()).getData();

        var response = serviceWithToken("invalid-token").deleteUserByEmail(created.getEmail());

        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(response.getStatusCode()).isEqualTo(401);
            softly.assertThat(response.isSuccess()).isFalse();
            softly.assertThat(response.getError()).isNotNull();
            if (response.getError() != null) {
                softly.assertThat(response.getError().error()).isNotBlank();
            }
        });
    }
}
