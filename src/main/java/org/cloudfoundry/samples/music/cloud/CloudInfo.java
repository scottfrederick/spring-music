package org.cloudfoundry.samples.music.cloud;

import org.cloudfoundry.runtime.env.AbstractServiceInfo;
import org.cloudfoundry.runtime.env.CloudEnvironment;
import org.cloudfoundry.runtime.env.MongoServiceInfo;
import org.cloudfoundry.runtime.env.RdbmsServiceInfo;
import org.cloudfoundry.runtime.env.RedisServiceInfo;
import org.cloudfoundry.runtime.service.document.MongoServiceCreator;
import org.cloudfoundry.runtime.service.keyvalue.RedisServiceCreator;
import org.cloudfoundry.runtime.service.relational.RdbmsServiceCreator;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.redis.connection.RedisConnectionFactory;

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
        List<MongoServiceInfo> serviceInfo = cloudEnvironment.getServiceInfos(MongoServiceInfo.class);
        MongoServiceCreator serviceCreator = new MongoServiceCreator();
        return serviceCreator.createService(serviceInfo.get(0));
    }

    public DataSource getRdbmsDataSource() {
        List<RdbmsServiceInfo> serviceInfo = cloudEnvironment.getServiceInfos(RdbmsServiceInfo.class);
        RdbmsServiceCreator serviceCreator = new RdbmsServiceCreator();
        return serviceCreator.createService(serviceInfo.get(0));
    }

    public RedisConnectionFactory getRedisFactory() {
        List<RedisServiceInfo> serviceInfo = cloudEnvironment.getServiceInfos(RedisServiceInfo.class);
        RedisServiceCreator serviceCreator = new RedisServiceCreator();
        return serviceCreator.createService(serviceInfo.get(0));
    }

    public AbstractServiceInfo[] getAllServiceInfos() {
        List<AbstractServiceInfo> serviceInfos = new ArrayList<AbstractServiceInfo>();

        serviceInfos.addAll(cloudEnvironment.getServiceInfos(RdbmsServiceInfo.class));
        serviceInfos.addAll(cloudEnvironment.getServiceInfos(MongoServiceInfo.class));
        serviceInfos.addAll(cloudEnvironment.getServiceInfos(RedisServiceInfo.class));

        return serviceInfos.toArray(new AbstractServiceInfo[serviceInfos.size()]);
    }

}
