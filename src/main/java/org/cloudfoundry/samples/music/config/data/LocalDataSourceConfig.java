package org.cloudfoundry.samples.music.config.data;

import org.hibernate.dialect.H2Dialect;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
@Profile("in-memory")
@EnableJpaRepositories("org.cloudfoundry.samples.music.repositories.jpa")
public class LocalDataSourceConfig extends RdbmsDataSourceConfig {
    @Bean
    public DataSource dataSource() {
        return new EmbeddedDatabaseBuilder()
                .setName("music")
                .setType(EmbeddedDatabaseType.H2)
                .build();
    }

    @Bean(name = "entityManagerFactory")
    public LocalContainerEntityManagerFactoryBean localContainerEntityManagerFactoryBean(DataSource dataSource) {
        Map<String, String> properties = new HashMap<String, String>();
        properties.put(org.hibernate.cfg.Environment.HBM2DDL_AUTO, "create");
        properties.put(org.hibernate.cfg.Environment.DIALECT, H2Dialect.class.getName());
        properties.put(org.hibernate.cfg.Environment.SHOW_SQL, "true");

        return createEntityManagerFactoryBean(dataSource, properties);
    }

    @Bean(name = "transactionManager")
    public JpaTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }
}
