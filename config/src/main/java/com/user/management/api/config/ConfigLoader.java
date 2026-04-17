package com.user.management.api.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.eclipse.microprofile.config.spi.ConfigProviderResolver;
import org.jboss.logging.Logger;

import io.smallrye.config.PropertiesConfigSource;
import io.smallrye.config.SmallRyeConfigBuilder;

public class ConfigLoader {

    private static final Logger LOG = Logger.getLogger(ConfigLoader.class);

    private static final String DEFAULT_PROPERTIES = "application.properties";

    private static final String ENV_PROPERTY_KEY = "env";

    public static <T> T load(Class<T> mappingClass) {

        SmallRyeConfigBuilder builder = (SmallRyeConfigBuilder) ConfigProviderResolver.instance().getBuilder();

        addPropertyFileIfExists(builder, DEFAULT_PROPERTIES);

        String env = resolveEnv();

        if (env != null && !env.isEmpty()) {
            String envFile = String.format("application-%s.properties", env.trim());
            addPropertyFileIfExists(builder, envFile);
        } else {
            LOG.info("Variable 'env' is not configured");
        }

        builder.withMapping(mappingClass);

        return builder.build().getConfigMapping(mappingClass);
    }

    //You can send via -Denv=dev or env=dev
    private static String resolveEnv() {

        String env = System.getProperty(ENV_PROPERTY_KEY);

        if (env == null || env.isEmpty()) {
            env = System.getenv(ENV_PROPERTY_KEY);
        }

        return env;
    }

    private static void addPropertyFileIfExists(SmallRyeConfigBuilder builder, String resource) {

        try (InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream(resource)) {
            if (in != null) {
                Properties props = new Properties();
                props.load(in);
                PropertiesConfigSource source = new PropertiesConfigSource(props, resource);
                builder.withSources(source);
                LOG.infof("Config resource loaded: %s", resource);
            } else {
                LOG.debugf("Config resource not found: %s", resource);
            }
        } catch (IOException e) {
            LOG.errorf(e, "Error to load config resource %s", resource);
        }
    }
}
