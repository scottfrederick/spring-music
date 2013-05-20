package org.cloudfoundry.samples.music.config;

import org.cloudfoundry.samples.music.domain.Album;
import org.hibernate.ejb.HibernatePersistence;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;

import javax.sql.DataSource;
import java.util.Map;

public class DataSourceConfig {
    public LocalContainerEntityManagerFactoryBean createEntityManagerFactoryBean(DataSource dataSource, Map<String, String> p) {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(dataSource);
        em.setPackagesToScan(Album.class.getPackage().getName());
        em.setPersistenceProvider(new HibernatePersistence());
        em.setJpaPropertyMap(p);
        return em;
    }
}
