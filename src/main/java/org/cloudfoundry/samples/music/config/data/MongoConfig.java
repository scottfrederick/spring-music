package org.cloudfoundry.samples.music.config.data;

import com.mongodb.MongoClient;
import org.cloudfoundry.samples.music.repositories.mongodb.MongoAlbumRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.Cloud;
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
public class MongoConfig {

    @Autowired(required = false)
    private Cloud cloud = null;

    @Bean
    public MongoDbFactory mongoDbFactory() {
        if (cloud != null) {
            return cloud.getSingletonServiceConnector(MongoDbFactory.class, null);
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
