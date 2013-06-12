package org.cloudfoundry.samples.music.config.data;

import org.cloudfoundry.samples.music.cloud.CloudInfo;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import javax.sql.DataSource;

@Configuration
@Profile("rdbms")
@EnableJpaRepositories("org.cloudfoundry.samples.music.repositories.jpa")
public class RdbmsDataSourceConfig {

    @Bean
    public DataSource dataSource() {
        CloudInfo cloudInfo = new CloudInfo();
        return cloudInfo.getRdbmsDataSource();
    }
}
