package com.user.management.api.testing.core.config;

import com.user.management.api.config.ConfigLoader;

public class Config {

    private static final ConfigMap instance = ConfigLoader.load(ConfigMap.class);

    private Config() {}

    public static ConfigMap get() {

        return instance;
    }
}
