package org.cloudfoundry.samples.music.config.data;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import javax.sql.DataSource;

@Configuration
@Profile("in-memory")
@EnableJpaRepositories("org.cloudfoundry.samples.music.repositories.jpa")
public class H2DataSourceConfig extends AbstractLocalDataSourceConfig {
    @Bean
    public DataSource dataSource() {
        return new EmbeddedDatabaseBuilder()
                .setName("music")
                .setType(EmbeddedDatabaseType.H2)
                .build();
    }
}
