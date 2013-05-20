package org.cloudfoundry.samples.music.config;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.cloudfoundry.runtime.env.CloudEnvironment;
import org.cloudfoundry.runtime.env.RdbmsServiceInfo;
import org.cloudfoundry.runtime.service.relational.RdbmsServiceCreator;
import org.hibernate.dialect.MySQL5Dialect;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;

@Configuration
@Profile("mysql")
@EnableJpaRepositories("org.cloudfoundry.samples.music.repositories")
public class CloudDataSourceConfig extends DataSourceConfig {

    private CloudEnvironment cloudEnvironment = new CloudEnvironment();

    @Bean
    public DataSource dataSource() {
        if (cloudEnvironment.isCloudFoundry()) {
            Collection<RdbmsServiceInfo> service = cloudEnvironment.getServiceInfos(RdbmsServiceInfo.class);
            RdbmsServiceCreator dataSourceCreator = new RdbmsServiceCreator();
            return dataSourceCreator.createService(service.iterator().next());
        } else {
            BasicDataSource dataSource = new BasicDataSource();
            dataSource.setUrl("jdbc:mysql://localhost/music");
            dataSource.setDriverClassName("com.mysql.jdbc.Driver");
            dataSource.setUsername("");
            dataSource.setPassword("");
            return dataSource;
        }
    }

    @Bean(name = "entityManagerFactory")
    public LocalContainerEntityManagerFactoryBean localContainerEntityManagerFactoryBean(DataSource dataSource) {
        Map<String, String> properties = new HashMap<String, String>();
        properties.put(org.hibernate.cfg.Environment.HBM2DDL_AUTO, "create");
        properties.put(org.hibernate.cfg.Environment.DIALECT, MySQL5Dialect.class.getName());
        properties.put(org.hibernate.cfg.Environment.SHOW_SQL, "true");

        return createEntityManagerFactoryBean(dataSource, properties);
    }
}

