package org.cloudfoundry.samples.music.config.data;

import org.cloudfoundry.samples.music.cloud.CloudInfo;
import org.hibernate.dialect.PostgreSQL82Dialect;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
@Profile("postgres")
@EnableJpaRepositories("org.cloudfoundry.samples.music.repositories.jpa")
public class PostgresDataSourceConfig extends RdbmsDataSourceConfig {

    @Bean
    public DataSource dataSource() {
        CloudInfo cloudInfo = new CloudInfo();

        if (cloudInfo.isCloud()) {
            return cloudInfo.getPostgresDataSource();
        } else {
            return createBasicDataSource("jdbc:postgresql://localhost/music", "org.postgresql.Driver",
                    "postgres", "postgres");
        }
    }


    @Bean(name = "entityManagerFactory")
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource) {
        Map<String, String> properties = new HashMap<String, String>();
        properties.put(org.hibernate.cfg.Environment.HBM2DDL_AUTO, "create");
        properties.put(org.hibernate.cfg.Environment.DIALECT, PostgreSQL82Dialect.class.getName());
        properties.put(org.hibernate.cfg.Environment.SHOW_SQL, "true");

        return createEntityManagerFactoryBean(dataSource, properties);
    }

    @Bean(name = "transactionManager")
    public JpaTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }
}
