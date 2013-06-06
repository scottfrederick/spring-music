package org.cloudfoundry.samples.music.cloud;

import com.mongodb.MongoURI;
import org.cloudfoundry.runtime.env.CloudEnvironment;
import org.cloudfoundry.runtime.env.CloudServiceException;
import org.cloudfoundry.runtime.env.MongoServiceInfo;
import org.cloudfoundry.runtime.env.MysqlServiceInfo;
import org.cloudfoundry.runtime.env.PostgresqlServiceInfo;
import org.cloudfoundry.runtime.env.RedisServiceInfo;
import org.cloudfoundry.runtime.service.document.MongoServiceCreator;
import org.cloudfoundry.runtime.service.keyvalue.RedisServiceCreator;
import org.cloudfoundry.runtime.service.relational.MysqlServiceCreator;
import org.cloudfoundry.runtime.service.relational.PostgresqlServiceCreator;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.data.redis.connection.RedisConnectionFactory;

import javax.sql.DataSource;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * There is much ugliness in this class. This is temporary until the cloudfoundry-runtime library has been updated
 * to support v2 format of service credentials. You've been warned.
 */
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
        Map<String, Object> mongoDbServiceInfo = getServiceInfoForType("mongodb");
        MongoDbFactory mongoDbFactory = getMongoLabsFactory(mongoDbServiceInfo);

        if (mongoDbFactory != null)
            return mongoDbFactory;

        MongoServiceInfo serviceInfo = new MongoServiceInfo(mongoDbServiceInfo);
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

    public RedisConnectionFactory getRedisFactory() {
        RedisServiceInfo serviceInfo = new RedisServiceInfo((getServiceInfoForType("redis")));
        RedisServiceCreator serviceCreator = new RedisServiceCreator();
        return serviceCreator.createService(serviceInfo);
    }

    public Map<String, Object> getServiceInfoForType(String serviceType) {
        for (Map<String, Object> service : cloudEnvironment.getServices()) {
            if (service.get("name").toString().contains(serviceType)) {
                return fixupServiceInfo(service);
            }
        }

        throw new RuntimeException("Expected exactly one service containing the name [" +
                serviceType + "] to be bound to the application.");
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> fixupServiceInfo(Map<String, Object> serviceInfo) {
        Map<String, Object> credentials = (Map<String, Object>) serviceInfo.get("credentials");
        if (credentials != null) {
            parseUriCredentials(credentials);

            Object port = credentials.get("port");
            if (port != null) {
                // if port is present in credentials, make sure it is an Integer
                credentials.put("port", Integer.valueOf(port.toString()));
            }
        }

        return serviceInfo;
    }

    private void parseUriCredentials(Map<String, Object> credentials) {
        if (credentials.containsKey("uri")) {
            String uriCredential = (String) credentials.get("uri");
            try {
                URI uri = new URI(uriCredential);

                credentials.put("hostname", uri.getHost());
                credentials.put("port", uri.getPort());

                String userInfo = uri.getRawUserInfo();
                String[] usernamePassword = userInfo.split(":");
                credentials.put("username", usernamePassword[0]);
                credentials.put("user", usernamePassword[0]);
                credentials.put("password", usernamePassword[1]);

                String path = uri.getRawPath().substring(1);
                credentials.put("name", path);
                credentials.put("db", path);
            } catch (URISyntaxException e) {
                throw new RuntimeException("Expected the single credential to be a value URI: " + uriCredential);
            }
        }
    }

    @SuppressWarnings("unchecked")
    private MongoDbFactory getMongoLabsFactory(Map<String, Object> mongoDbServiceInfo) {
        Map<String, Object> credentials = (Map<String, Object>) mongoDbServiceInfo.get("credentials");
        if (credentials == null) {
            return null;
        }

        String mongoLabsUri = (String) credentials.get("mongolabUri");
        if (mongoLabsUri == null) {
            return null;
        }

        try {
            return new SimpleMongoDbFactory(new MongoURI(mongoLabsUri));
        } catch (UnknownHostException e) {
            throw new CloudServiceException("Error creating MongoLabs connection: " + e);
        }
    }
}
