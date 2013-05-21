package org.cloudfoundry.samples.music.config;

import org.cloudfoundry.samples.music.config.data.LocalDataSourceConfig;
import org.cloudfoundry.samples.music.repositories.AlbumRepository;
import org.springframework.context.annotation.*;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.data.repository.init.JacksonRepositoryPopulatorFactoryBean;

@Configuration
@ComponentScan(basePackageClasses = {AlbumRepository.class, LocalDataSourceConfig.class})
public class RepositoryConfig {

    @Bean
    public JacksonRepositoryPopulatorFactoryBean repositoryPopulator() {
        Resource sourceData = new ClassPathResource("albums.json");

        JacksonRepositoryPopulatorFactoryBean factory = new JacksonRepositoryPopulatorFactoryBean();
        factory.setResources(new Resource[] { sourceData });

        return factory;
    }
}
