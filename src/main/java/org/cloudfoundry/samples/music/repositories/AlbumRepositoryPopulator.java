package org.cloudfoundry.samples.music.repositories;

import org.cloudfoundry.samples.music.domain.Album;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.data.repository.init.JacksonResourceReader;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
public class AlbumRepositoryPopulator implements ApplicationListener<ContextRefreshedEvent>, ApplicationContextAware {
    private final JacksonResourceReader resourceReader;
    private final Resource sourceData;

    private ApplicationContext applicationContext;

    public AlbumRepositoryPopulator() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        resourceReader = new JacksonResourceReader(mapper);
        sourceData = new ClassPathResource("albums.json");
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (event.getApplicationContext().equals(applicationContext)) {
            AlbumRepository albumRepository =
                    BeanFactoryUtils.beanOfTypeIncludingAncestors(applicationContext, AlbumRepository.class);

            if (albumRepository != null && albumRepository.count() == 0) {
                populate(albumRepository);
            }
        }

    }

    @SuppressWarnings("unchecked")
    public void populate(AlbumRepository repository) {
        Object entity = getEntityFromResource(sourceData);

        if (entity instanceof Collection) {
            for (Album album : (Collection<Album>) entity) {
                if (album != null) {
                    repository.save(album);
                }
            }
        } else {
            repository.save((Album) entity);
        }
    }

    private Object getEntityFromResource(Resource resource) {
        try {
            return resourceReader.readFrom(resource, this.getClass().getClassLoader());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
