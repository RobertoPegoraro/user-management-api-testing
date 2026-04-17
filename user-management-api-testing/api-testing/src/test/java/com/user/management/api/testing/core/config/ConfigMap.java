package com.user.management.api.testing.core.config;

import io.smallrye.config.ConfigMapping;
import io.smallrye.config.WithName;

@ConfigMapping
public interface ConfigMap {

    @WithName("baseUrl")
    String baseUrl();

    @WithName("token")
    String token();
}