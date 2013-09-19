package org.cloudfoundry.samples.music.cloud;

import org.springframework.cloud.cloudfoundry.CloudFoundryServiceInfoCreator;

import java.util.Map;

public class NewRelicServiceInfoCreator extends CloudFoundryServiceInfoCreator<NewRelicServiceInfo> {
    public NewRelicServiceInfoCreator() {
        super("newrelic");
        System.out.println("*********** NewRelicServiceInfoCreator instantiated");
    }

    @Override
    public boolean accept(Object serviceData) {
        @SuppressWarnings("unchecked")
        Map<String, Object> serviceDataMap = (Map<String, Object>) serviceData;

        return ((String) serviceDataMap.get("label")).startsWith("newrelic");
    }

    @Override
    @SuppressWarnings("unchecked")
    public NewRelicServiceInfo createServiceInfo(Object serviceData) {
        Map<String, Object> serviceDataMap = (Map<String, Object>) serviceData;

        String id = (String) serviceDataMap.get("name");

        Map<String, Object> credentials = (Map<String, Object>) serviceDataMap.get("credentials");
        String licenseKey = (String) credentials.get("licenseKey");

        return new NewRelicServiceInfo(id, licenseKey);
    }
}