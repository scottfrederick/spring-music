package org.cloudfoundry.samples.music.config;

import org.apache.commons.dbcp.BasicDataSource;
import org.cloudfoundry.runtime.env.CloudEnvironment;
import org.cloudfoundry.runtime.env.MysqlServiceInfo;
import org.cloudfoundry.runtime.service.relational.MysqlServiceCreator;
import org.cloudfoundry.samples.music.domain.Album;
import org.hibernate.ejb.HibernatePersistence;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;

import javax.sql.DataSource;
import java.util.Map;

public class RdbmsDataSourceConfig {

    public DataSource createDataSource(String serviceType, String jdbcUrl, String driverClass,
                                       String userName, String password) {
        CloudEnvironment cloudEnvironment = new CloudEnvironment();

        if (cloudEnvironment.isCloudFoundry()) {
            return createCloudDataSource(cloudEnvironment, serviceType);
        } else {
            return createBasicDataSource(jdbcUrl, driverClass, userName, password);
        }
    }

    private DataSource createCloudDataSource(CloudEnvironment cloudEnvironment, String serviceType) {
        for (Map<String, Object> service : cloudEnvironment.getServices()) {
            if (service.get("label").toString().contains(serviceType)) {
                MysqlServiceInfo serviceInfo = new MysqlServiceInfo(service);
                MysqlServiceCreator serviceCreator = new MysqlServiceCreator();
                return serviceCreator.createService(serviceInfo);
            }
        }

        throw new RuntimeException("config error");
    }

    private BasicDataSource createBasicDataSource(String jdbcUrl, String driverClass, String userName, String password) {
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setUrl(jdbcUrl);
        dataSource.setDriverClassName(driverClass);
        dataSource.setUsername(userName);
        dataSource.setPassword(password);
        return dataSource;
    }

    public LocalContainerEntityManagerFactoryBean createEntityManagerFactoryBean(DataSource dataSource, Map<String, String> p) {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(dataSource);
        em.setPackagesToScan(Album.class.getPackage().getName());
        em.setPersistenceProvider(new HibernatePersistence());
        em.setJpaPropertyMap(p);
        return em;
    }
}
