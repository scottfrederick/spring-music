package org.cloudfoundry.samples.music.config;

import org.cloudfoundry.runtime.env.*;
import org.cloudfoundry.samples.music.cloud.CloudInfo;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SpringApplicationContextInitializer implements ApplicationContextInitializer<AnnotationConfigWebApplicationContext> {
    private static final Map<Class<? extends AbstractServiceInfo>, String> serviceTypeToProfileName = new
            HashMap<Class<? extends AbstractServiceInfo>, String>();
    private static final List<String> validProfiles = Arrays.asList("mysql", "postgres", "mongodb", "redis");

    public static final String IN_MEMORY_PROFILE = "in-memory";

    private CloudInfo cloudInfo;
    private ConfigurableEnvironment appEnvironment;

    static {
        serviceTypeToProfileName.put(MongoServiceInfo.class, "mongodb");
        serviceTypeToProfileName.put(RdbmsServiceInfo.class, "rdbms");
        serviceTypeToProfileName.put(RedisServiceInfo.class, "redis");
    }

    @Override
    public void initialize(AnnotationConfigWebApplicationContext applicationContext) {
        cloudInfo = new CloudInfo();
        appEnvironment = applicationContext.getEnvironment();

        String persistenceProfile = IN_MEMORY_PROFILE;

        String[] activeProfiles;
        if (isCloudFoundry()) {
            activeProfiles = getCloudProfiles();
        } else {
            activeProfiles = getActiveProfiles();
        }

        if (activeProfiles.length > 0) {
            if (activeProfiles.length != 1) {
                throw new RuntimeException(getConfigurationErrorMessage());
            }

            persistenceProfile = activeProfiles[0];
        }

        appEnvironment.addActiveProfile(persistenceProfile);
    }

    private boolean isCloudFoundry() {
        return cloudInfo.isCloud();
    }

    public String[] getCloudProfiles() {
        List<String> profiles = new ArrayList<String>();

        AbstractServiceInfo[] serviceInfos = cloudInfo.getAllServiceInfos();
        for (AbstractServiceInfo serviceInfo : serviceInfos) {
            if (serviceTypeToProfileName.containsKey(serviceInfo.getClass())) {
                profiles.add(serviceTypeToProfileName.get(serviceInfo.getClass()));
            }
        }

        return profiles.toArray(new String[profiles.size()]);
    }

    private String[] getActiveProfiles() {
        List<String> serviceProfiles = new ArrayList<String>();

        for (String profile : appEnvironment.getActiveProfiles()) {
            if (validProfiles.contains(profile)) {
                serviceProfiles.add(profile);
            }
        }

        return serviceProfiles.toArray(new String[serviceProfiles.size()]);
    }

    private String getConfigurationErrorMessage() {
        if (isCloudFoundry())
            return "Only one service of the following types may be bound to this application: " +
                    serviceTypeToProfileName.values().toString() + ". " +
                    "These services are bound to the application: " +
                    Arrays.toString(getCloudProfiles());
        else
            return "Only one active Spring profile may be set among the following: " +
                    validProfiles.toString() + ". " +
                    "These profiles are active: " +
                    Arrays.toString(getActiveProfiles());
    }
}
