package org.cloudfoundry.samples.music.config.data;

import org.apache.commons.dbcp.BasicDataSource;
import org.cloudfoundry.samples.music.domain.Album;
import org.hibernate.ejb.HibernatePersistence;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;

import javax.sql.DataSource;
import java.util.Map;

public class RdbmsDataSourceConfig {

    protected BasicDataSource createBasicDataSource(String jdbcUrl, String driverClass, String userName, String password) {
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setUrl(jdbcUrl);
        dataSource.setDriverClassName(driverClass);
        dataSource.setUsername(userName);
        dataSource.setPassword(password);
        return dataSource;
    }

    protected LocalContainerEntityManagerFactoryBean createEntityManagerFactoryBean(DataSource dataSource, Map<String, String> p) {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(dataSource);
        em.setPackagesToScan(Album.class.getPackage().getName());
        em.setPersistenceProvider(new HibernatePersistence());
        em.setJpaPropertyMap(p);
        return em;
    }
}
