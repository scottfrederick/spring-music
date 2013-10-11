package org.cloudfoundry.samples.music.config.data;

import org.cloudfoundry.samples.music.repositories.mongodb.MongoAlbumRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@Profile("mongodb")
@EnableMongoRepositories(basePackageClasses = {MongoAlbumRepository.class})
public class MongoConfig {

    @Bean
    public MongoTemplate mongoTemplate(MongoDbFactory mongoDbFactory) {
        return new MongoTemplate(mongoDbFactory);
    }

}
