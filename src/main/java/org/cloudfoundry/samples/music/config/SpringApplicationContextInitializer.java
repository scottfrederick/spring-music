package org.cloudfoundry.samples.music.config;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.data.mongo.MongoRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.cloud.bindings.Binding;
import org.springframework.cloud.bindings.Bindings;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.Profiles;
import org.springframework.core.env.PropertySource;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SpringApplicationContextInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    private static final Log logger = LogFactory.getLog(SpringApplicationContextInitializer.class);

    private static final List<String> supportedServiceTypes =
            Arrays.asList("mongodb", "mysql", "oracle", "postgresql", "redis", "sqlserver");

    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        ConfigurableEnvironment appEnvironment = applicationContext.getEnvironment();

        validateActiveProfiles(appEnvironment);

        addCloudProfile(appEnvironment);

        excludeAutoConfiguration(appEnvironment);
    }

    private void addCloudProfile(ConfigurableEnvironment appEnvironment) {
        List<String> profiles = new ArrayList<>();

        List<String> bindingTypes;
        try {
            Class.forName("org.springframework.cloud.bindings.Bindings");
            bindingTypes = new Bindings().getBindings().stream()
                    .map(Binding::getType)
                    .collect(Collectors.toList());
            logger.info("Found bindings [" + StringUtils.collectionToCommaDelimitedString(bindingTypes) + "]");
        } catch (ClassNotFoundException e) {
            logger.warn("Spring Cloud Bindings is not on the classpath, bindings will not be detected");
            bindingTypes = Collections.emptyList();
        }

        for (String serviceType : bindingTypes) {
            if (supportedServiceTypes.contains(serviceType)) {
                profiles.add(serviceType);
            }
        }

        if (profiles.size() > 1) {
            throw new IllegalStateException(
                    "Only one service of the following types may be bound to this application: [" +
                            StringUtils.collectionToCommaDelimitedString(supportedServiceTypes) + "]. " +
                            "These services are bound to the application: [" +
                            StringUtils.collectionToCommaDelimitedString(profiles) + "]");
        }

        if (profiles.size() > 0) {
            logger.info("Setting service profile " + profiles.get(0));
            appEnvironment.addActiveProfile(profiles.get(0));
        }
    }

    private void validateActiveProfiles(ConfigurableEnvironment appEnvironment) {
        List<String> serviceProfiles = Stream.of(appEnvironment.getActiveProfiles())
                .filter(supportedServiceTypes::contains)
                .collect(Collectors.toList());

        if (serviceProfiles.size() > 1) {
            throw new IllegalStateException("Only one active Spring profile may be set among the following: " +
                    StringUtils.collectionToCommaDelimitedString(supportedServiceTypes) + ". " +
                    "These profiles are active: [" +
                    StringUtils.collectionToCommaDelimitedString(serviceProfiles) + "]");
        }
    }

    private void excludeAutoConfiguration(ConfigurableEnvironment environment) {
        List<String> exclude = new ArrayList<>();
        if (environment.acceptsProfiles(Profiles.of("redis"))) {
            excludeDataSourceAutoConfiguration(exclude);
            excludeMongoAutoConfiguration(exclude);
        } else if (environment.acceptsProfiles(Profiles.of("mongodb"))) {
            excludeDataSourceAutoConfiguration(exclude);
            excludeRedisAutoConfiguration(exclude);
        } else {
            excludeMongoAutoConfiguration(exclude);
            excludeRedisAutoConfiguration(exclude);
        }

        Map<String, Object> properties = Collections.singletonMap("spring.autoconfigure.exclude",
                StringUtils.collectionToCommaDelimitedString(exclude));

        PropertySource<?> propertySource = new MapPropertySource("springMusicAutoConfig", properties);

        environment.getPropertySources().addFirst(propertySource);
    }

    private void excludeDataSourceAutoConfiguration(List<String> exclude) {
        exclude.add(DataSourceAutoConfiguration.class.getName());
    }

    private void excludeMongoAutoConfiguration(List<String> exclude) {
        exclude.addAll(Arrays.asList(
                MongoAutoConfiguration.class.getName(),
                MongoDataAutoConfiguration.class.getName(),
                MongoRepositoriesAutoConfiguration.class.getName()
        ));
    }

    private void excludeRedisAutoConfiguration(List<String> exclude) {
        exclude.addAll(Arrays.asList(
                RedisAutoConfiguration.class.getName(),
                RedisRepositoriesAutoConfiguration.class.getName()
        ));
    }
}
