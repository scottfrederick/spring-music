package org.cloudfoundry.samples.music.config.data;

import org.hibernate.dialect.PostgreSQL82Dialect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.Cloud;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

@Configuration
@Profile("postgres")
@EnableJpaRepositories("org.cloudfoundry.samples.music.repositories.jpa")
public class PostgresDataSourceConfig extends AbstractDataSourceConfig {

    @Autowired(required = false)
    private Cloud cloud = null;

    @Bean
    public DataSource dataSource() {
        if (cloud != null) {
            return cloud.getSingletonServiceConnector(DataSource.class, null);
        } else {
            return createBasicDataSource("jdbc:postgresql://localhost/music",
                    "org.postgresql.Driver", "postgres", "postgres");
        }
    }

    @Bean(name = "entityManagerFactory")
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource) {
        return createEntityManagerFactoryBean(dataSource, PostgreSQL82Dialect.class.getName());
    }

    @Bean(name = "transactionManager")
    public JpaTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }
}
