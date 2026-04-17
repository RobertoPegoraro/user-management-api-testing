package com.user.management.api.testing.utils;

import java.util.UUID;

import net.datafaker.Faker;

public class FakerUtils {

    private static final Faker faker = new Faker();

    private FakerUtils() {}

    public static String generateName() {

        return faker.name().fullName();
    }

    public static String generateEmail() {

        return "test-" + UUID.randomUUID().toString().replace("-", "").substring(0, 12) + "@example.com";
    }

    public static int generateAge() {

        return faker.number().numberBetween(1, 151);
    }
}
