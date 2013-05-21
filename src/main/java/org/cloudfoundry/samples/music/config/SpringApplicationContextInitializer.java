package org.cloudfoundry.samples.music.config;

import org.cloudfoundry.runtime.env.CloudEnvironment;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class SpringApplicationContextInitializer implements ApplicationContextInitializer<AnnotationConfigWebApplicationContext> {

    public static final String IN_MEMORY_PROFILE = "in-memory";

    private static final String[] serviceTypes = {"mysql", "postgres", "mongodb", "redis"};

    private CloudEnvironment cloudEnvironment;
    private ConfigurableEnvironment appEnvironment;

    @Override
    public void initialize(AnnotationConfigWebApplicationContext applicationContext) {
        cloudEnvironment = new CloudEnvironment();
        appEnvironment = applicationContext.getEnvironment();

        String persistenceProfile = IN_MEMORY_PROFILE;

        String[] activeServices;
        if (isCloudFoundry()) {
            activeServices = getCloudServices();
        }
        else {
            activeServices = getProfileServices();
        }

        String[] services = mapServices(activeServices);

        if (services.length > 0) {
            if (services.length != 1) {
                throw new RuntimeException(getConfigurationErrorMessage());
            }

            persistenceProfile = services[0];
        }

        appEnvironment.addActiveProfile(persistenceProfile);
    }

    private boolean isCloudFoundry() {
        return cloudEnvironment.isCloudFoundry();
    }

    private String[] getCloudServices() {
        List<String> foundServices = new ArrayList<String>();

        for (Map<String, Object> service : cloudEnvironment.getServices()) {
            foundServices.add(service.get("name").toString());
        }

        return foundServices.toArray(new String[foundServices.size()]);
    }

    private String[] getProfileServices() {
        return appEnvironment.getActiveProfiles();
    }

    private String[] mapServices(String[] services) {
        List<String> foundServices = new ArrayList<String>();

        for (String service : services) {
            for (String serviceType : serviceTypes) {
                if (service.contains(serviceType)) {
                    foundServices.add(serviceType);
                }
            }
        }

        return foundServices.toArray(new String[foundServices.size()]);
    }

    private String getConfigurationErrorMessage() {
        if (isCloudFoundry())
            return "Only one service of the following types may be bound to this application: " +
                    Arrays.toString(serviceTypes) + ". " +
                    "These services are bound to the application: " +
                    Arrays.toString(getCloudServices());
        else
            return "Only one active Spring profile may be set among the following: " +
                    Arrays.toString(serviceTypes) + ". " +
                    "These profiles are active: " +
                    Arrays.toString(appEnvironment.getActiveProfiles());
    }
}
