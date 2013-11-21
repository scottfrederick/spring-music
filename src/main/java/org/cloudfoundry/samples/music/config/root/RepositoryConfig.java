package org.cloudfoundry.samples.music.config.root;

import org.cloudfoundry.samples.music.config.data.LocalJpaRepositoryConfig;
import org.cloudfoundry.samples.music.repositories.AlbumRepository;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackageClasses = {AlbumRepository.class, LocalJpaRepositoryConfig.class})
public class RepositoryConfig {
}

