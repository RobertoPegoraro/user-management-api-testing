package com.user.management.api.testing.tests;

import java.util.List;

import org.assertj.core.api.SoftAssertions;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.user.management.api.testing.core.config.Config;
import com.user.management.api.testing.model.User;

public class ListUsersEmptyTest extends BaseTest {

    @Override
    @BeforeClass(groups = "sequential")
    protected void setup() {

        userService = serviceWithToken(Config.get().token());
        List<User> users = userService.listUsers().getData();
        if (users != null) {
            users.forEach(user -> userService.deleteUserByEmail(user.getEmail()));
        }
    }

    @Test(groups = "sequential")
    void listUsers_shouldReturnEmptyList() {

        var response = userService.listUsers();
        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(response.getStatusCode()).isEqualTo(200);
            softly.assertThat(response.getData()).isEmpty();
        });
    }
}
