package org.cloudfoundry.samples.music.config.data;

import org.hibernate.dialect.MySQL5Dialect;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@Profile("mysql")
@EnableJpaRepositories("org.cloudfoundry.samples.music.repositories.jpa")
public class MySqlRepositoryConfig extends AbstractJpaRepositoryConfig {

    protected String getHibernateDialect() {
        return MySQL5Dialect.class.getName();
    }

}
