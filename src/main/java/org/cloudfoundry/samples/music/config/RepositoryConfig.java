package org.cloudfoundry.samples.music.config;

import org.cloudfoundry.samples.music.repositories.AlbumRepository;
import org.springframework.context.annotation.*;
import org.springframework.orm.jpa.JpaTransactionManager;

import javax.persistence.EntityManagerFactory;

@Configuration
@PropertySource("/config.properties")
@ComponentScan(basePackageClasses = {AlbumRepository.class})
public class RepositoryConfig {

    @Bean(name = "transactionManager")
    public JpaTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }
}
