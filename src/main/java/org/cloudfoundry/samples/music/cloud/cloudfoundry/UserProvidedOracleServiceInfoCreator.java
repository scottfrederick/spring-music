package org.cloudfoundry.samples.music.cloud;

import org.springframework.cloud.cloudfoundry.CloudFoundryServiceInfoCreator;

import java.util.Map;

public class UserProvidedOracleServiceInfoCreator extends CloudFoundryServiceInfoCreator<OracleServiceInfo> {
    public UserProvidedOracleServiceInfoCreator() {
        super("user-provided");
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean accept(Map<String, Object> serviceData) {
        String label = (String) serviceData.get("label");

        if (label.equals(getTag())) {
            Map<String, Object> credentials = (Map<String, Object>) serviceData.get("credentials");
            String uri = (String) credentials.get("uri");

            if (uri != null && uri.startsWith("oracle:")) {
                return true;
            }
        }

        return false;
    }

    @Override
    @SuppressWarnings("unchecked")
    public OracleServiceInfo createServiceInfo(Map<String, Object> serviceData) {
        String id = (String) serviceData.get("name");
        Map<String, Object> credentials = (Map<String, Object>) serviceData.get("credentials");
        String uri = credentials.get("uri").toString();
        return new OracleServiceInfo(id, uri);
    }
}
