package org.cloudfoundry.samples.music.config;

import org.springframework.cloud.Cloud;
import org.springframework.cloud.CloudException;
import org.springframework.cloud.CloudFactory;
import org.springframework.cloud.service.ServiceInfo;
import org.springframework.cloud.service.common.MongoServiceInfo;
import org.springframework.cloud.service.common.MysqlServiceInfo;
import org.springframework.cloud.service.common.PostgresqlServiceInfo;
import org.springframework.cloud.service.common.RedisServiceInfo;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.util.StringUtils;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

import java.util.*;

public class SpringApplicationContextInitializer implements ApplicationContextInitializer<AnnotationConfigWebApplicationContext> {
    private static final Map<Class<? extends ServiceInfo>, String> serviceTypeToProfileName =
            new HashMap<Class<? extends ServiceInfo>, String>();
    private static final List<String> validProfiles = Arrays.asList("mysql", "postgres", "mongodb", "redis");

    public static final String IN_MEMORY_PROFILE = "in-memory";

    static {
        serviceTypeToProfileName.put(MongoServiceInfo.class, "mongodb");
        serviceTypeToProfileName.put(PostgresqlServiceInfo.class, "postgres");
        serviceTypeToProfileName.put(MysqlServiceInfo.class, "mysql");
        serviceTypeToProfileName.put(RedisServiceInfo.class, "redis");
    }

    @Override
    public void initialize(AnnotationConfigWebApplicationContext applicationContext) {
        Cloud cloud = getCloud();

        ConfigurableEnvironment appEnvironment = applicationContext.getEnvironment();

        String persistenceProfile = getCloudProfile(cloud);
        if (persistenceProfile == null) {
            persistenceProfile = getActiveProfile(appEnvironment);
        }
        if (persistenceProfile == null) {
            persistenceProfile = IN_MEMORY_PROFILE;
        }

        appEnvironment.addActiveProfile(persistenceProfile);
    }

    public String getCloudProfile(Cloud cloud) {
        if (cloud == null) {
            return null;
        }

        List<String> profiles = new ArrayList<String>();

        List<ServiceInfo> serviceInfos = cloud.getServiceInfos();
        for (ServiceInfo serviceInfo : serviceInfos) {
            if (serviceTypeToProfileName.containsKey(serviceInfo.getClass())) {
                profiles.add(serviceTypeToProfileName.get(serviceInfo.getClass()));
            }
        }

        if (profiles.size() > 1) {
            throw new IllegalStateException(
                    "Only one service of the following types may be bound to this application: " +
                            serviceTypeToProfileName.values().toString() + ". " +
                            "These services are bound to the application: [" +
                            StringUtils.collectionToCommaDelimitedString(profiles) + "]");
        }

        return (profiles.size() > 0) ? profiles.get(0) : null;
    }

    private Cloud getCloud() {
        try {
            CloudFactory cloudFactory = new CloudFactory();
            return cloudFactory.getCloud();
        } catch (CloudException ce) {
            return null;
        }
    }

    private String getActiveProfile(ConfigurableEnvironment appEnvironment) {
        List<String> serviceProfiles = new ArrayList<String>();

        for (String profile : appEnvironment.getActiveProfiles()) {
            if (validProfiles.contains(profile)) {
                serviceProfiles.add(profile);
            }
        }

        if (serviceProfiles.size() > 1) {
            throw new IllegalStateException("Only one active Spring profile may be set among the following: " +
                    validProfiles.toString() + ". " +
                    "These profiles are active: [" +
                    StringUtils.collectionToCommaDelimitedString(serviceProfiles) + "]");
        }

        return (serviceProfiles.size() > 0) ? serviceProfiles.get(0) : null;
    }
}
