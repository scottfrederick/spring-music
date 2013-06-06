package org.cloudfoundry.samples.music.config.root;

import org.cloudfoundry.samples.music.config.data.LocalDataSourceConfig;
import org.cloudfoundry.samples.music.repositories.AlbumRepository;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackageClasses = {AlbumRepository.class, LocalDataSourceConfig.class})
public class RepositoryConfig {
}

