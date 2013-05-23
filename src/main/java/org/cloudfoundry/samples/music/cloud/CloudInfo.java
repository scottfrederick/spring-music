package org.cloudfoundry.samples.music.cloud;

import org.cloudfoundry.runtime.env.CloudEnvironment;
import org.cloudfoundry.runtime.env.MongoServiceInfo;
import org.cloudfoundry.runtime.env.MysqlServiceInfo;
import org.cloudfoundry.runtime.env.PostgresqlServiceInfo;
import org.cloudfoundry.runtime.service.document.MongoServiceCreator;
import org.cloudfoundry.runtime.service.relational.MysqlServiceCreator;
import org.cloudfoundry.runtime.service.relational.PostgresqlServiceCreator;
import org.springframework.data.mongodb.MongoDbFactory;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CloudInfo {
    private CloudEnvironment cloudEnvironment = new CloudEnvironment();

    public boolean isCloud() {
        return cloudEnvironment.isCloudFoundry();
    }

    public String[] getServiceNames() {
        if (cloudEnvironment.isCloudFoundry()) {
            List<Map<String, Object>> services = cloudEnvironment.getServices();

            List<String> names = new ArrayList<String>();
            for (Map<String, Object> service : services) {
                names.add(service.get("name").toString());
            }
            return names.toArray(new String[names.size()]);
        } else {
            return new String[]{};
        }
    }

    public MongoDbFactory getMongoDbFactory() {
        MongoServiceInfo serviceInfo = new MongoServiceInfo(getServiceInfoForType("mongodb"));
        MongoServiceCreator serviceCreator = new MongoServiceCreator();
        return serviceCreator.createService(serviceInfo);
    }

    public DataSource getMySqlDataSource() {
        MysqlServiceInfo serviceInfo = new MysqlServiceInfo(getServiceInfoForType("mysql"));
        MysqlServiceCreator serviceCreator = new MysqlServiceCreator();
        return serviceCreator.createService(serviceInfo);
    }

    public DataSource getPostgresDataSource() {
        PostgresqlServiceInfo serviceInfo = new PostgresqlServiceInfo(getServiceInfoForType("postgres"));
        PostgresqlServiceCreator serviceCreator = new PostgresqlServiceCreator();
        return serviceCreator.createService(serviceInfo);
    }

    private Map<String, Object> getServiceInfoForType(String serviceType) {
        for (Map<String, Object> service : cloudEnvironment.getServices()) {
            if (service.get("name").toString().contains(serviceType)) {
                return service;
            }
        }

        throw new RuntimeException("Expected exactly one service containing the name [" +
                serviceType + "] to be bound to the application.");
    }
}
