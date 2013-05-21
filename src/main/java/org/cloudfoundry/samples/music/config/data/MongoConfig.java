package org.cloudfoundry.samples.music.config.data;

import com.mongodb.MongoClient;
import org.cloudfoundry.runtime.env.CloudEnvironment;
import org.cloudfoundry.runtime.env.MongoServiceInfo;
import org.cloudfoundry.runtime.service.document.MongoServiceCreator;
import org.cloudfoundry.samples.music.repositories.mongodb.MongoAlbumRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import java.net.UnknownHostException;

@Configuration
@Profile("mongodb")
@EnableMongoRepositories(basePackageClasses = {MongoAlbumRepository.class})
public class MongoConfig extends CloudServiceConfig {

    @Bean
    public MongoDbFactory mongoDbFactory() {
        CloudEnvironment cloudEnvironment = new CloudEnvironment();

        if (cloudEnvironment.isCloudFoundry()) {
            MongoServiceInfo serviceInfo = new MongoServiceInfo(getCloudServiceInfo("mongodb"));
            MongoServiceCreator serviceCreator = new MongoServiceCreator();
            return serviceCreator.createService(serviceInfo);
        } else {
            return createMongoDbFactory();
        }
    }

    private MongoDbFactory createMongoDbFactory() {
        try {
            return new SimpleMongoDbFactory(new MongoClient(), "music");
        } catch (UnknownHostException e) {
            throw new RuntimeException("Error creating MongoClient: " + e);
        }
    }

    @Bean
    public MongoTemplate mongoTemplate() {
        return new MongoTemplate(mongoDbFactory());
    }
}
