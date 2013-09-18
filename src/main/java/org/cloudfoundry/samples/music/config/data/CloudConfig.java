package org.cloudfoundry.samples.music.config.data;

import org.springframework.cloud.Cloud;
import org.springframework.cloud.CloudException;
import org.springframework.cloud.CloudFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("cloud")
public class CloudConfig {
    @Bean
    public Cloud getCloud() {
        try {
            CloudFactory cloudFactory = new CloudFactory();
            return cloudFactory.getCloud();
        } catch (CloudException ce) {
            return null;
        }
    }
}
