package org.cloudfoundry.samples.music.config.data;

import org.hibernate.dialect.PostgreSQL82Dialect;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@Profile("postgres")
@EnableJpaRepositories("org.cloudfoundry.samples.music.repositories.jpa")
public class PostgresRepositoryConfig extends AbstractJpaRepositoryConfig {

    protected String getHibernateDialect() {
        return PostgreSQL82Dialect.class.getName();
    }

}
