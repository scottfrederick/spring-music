package org.cloudfoundry.samples.music.config.data;

import org.hibernate.dialect.MySQL5Dialect;
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
@Profile("mysql")
@EnableJpaRepositories("org.cloudfoundry.samples.music.repositories.jpa")
public class MySqlDataSourceConfig extends RdbmsDataSourceConfig {

    @Bean
    public DataSource dataSource() {
        return createDataSource("mysql", "jdbc:mysql://localhost/music", "com.mysql.jdbc.Driver", "", "");
    }

    @Bean(name = "entityManagerFactory")
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource) {
        Map<String, String> properties = new HashMap<String, String>();
        properties.put(org.hibernate.cfg.Environment.HBM2DDL_AUTO, "create");
        properties.put(org.hibernate.cfg.Environment.DIALECT, MySQL5Dialect.class.getName());
        properties.put(org.hibernate.cfg.Environment.SHOW_SQL, "true");

        return createEntityManagerFactoryBean(dataSource, properties);
    }

    @Bean(name = "transactionManager")
    public JpaTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }
}

