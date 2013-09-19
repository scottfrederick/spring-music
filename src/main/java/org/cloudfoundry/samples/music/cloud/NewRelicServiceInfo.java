package org.cloudfoundry.samples.music.cloud;

import org.springframework.cloud.service.ServiceInfo;

public class NewRelicServiceInfo implements ServiceInfo {
    private final String id;
    private final String licenseKey;

    NewRelicServiceInfo(String id, String licenseKey) {
        this.id = id;
        this.licenseKey = licenseKey;
    }

    @Override
    @ServiceProperty
    public String getId() {
        return id;
    }

    @ServiceProperty
    String getLicenseKey() {
        return licenseKey;
    }
}