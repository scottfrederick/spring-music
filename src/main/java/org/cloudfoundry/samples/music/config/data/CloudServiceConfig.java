package org.cloudfoundry.samples.music.config.data;

import org.cloudfoundry.runtime.env.CloudEnvironment;

import java.util.Map;

public class CloudServiceConfig {

    protected Map<String, Object> getCloudServiceInfo(String serviceType) {
        CloudEnvironment cloudEnvironment = new CloudEnvironment();

        for (Map<String, Object> service : cloudEnvironment.getServices()) {
            if (service.get("name").toString().contains(serviceType)) {
                return service;
            }
        }

        throw new RuntimeException("Expected exactly one service containing the name [" +
                serviceType + "] to be bound to the application.");
    }
}
