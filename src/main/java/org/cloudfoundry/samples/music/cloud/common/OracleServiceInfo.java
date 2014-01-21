package org.cloudfoundry.samples.music.cloud;

import org.springframework.cloud.service.ServiceInfo.ServiceLabel;
import org.springframework.cloud.service.common.RelationalServiceInfo;

@ServiceLabel("oracle")
public class OracleServiceInfo extends RelationalServiceInfo {

    public OracleServiceInfo(String id, String url) {
        super(id, url, "oracle");
    }

    @Override
    public String getJdbcUrl() {
        return String.format("jdbc:%s:thin:%s/%s@%s:%d/%s",
                "oracle", getUserName(), getPassword(),
                getHost(), getPort(), getPath());
    }

}
