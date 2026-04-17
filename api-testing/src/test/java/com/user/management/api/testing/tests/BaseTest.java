package com.user.management.api.testing.tests;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;

import com.user.management.api.testing.service.UserService;
import com.user.management.api.testing.service.factory.UserServiceFactory;

@Listeners({
        io.qameta.allure.testng.AllureTestNg.class,
        com.user.management.api.testing.core.report.AllureLogListener.class
})
public abstract class BaseTest {

    protected UserService userService;

    protected UserService serviceWithNoToken() {

        return UserServiceFactory.getService();
    }

    protected UserService serviceWithToken(String token) {

        return UserServiceFactory.getServiceWithToken(token);
    }

    @BeforeClass
    protected void setup() {

        userService = serviceWithNoToken();
    }
}
