package org.cloudfoundry.samples.music.repositories;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.cloudfoundry.samples.music.domain.Album;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.init.Jackson2ResourceReader;

import java.util.Collection;

public class AlbumRepositoryPopulator implements ApplicationListener<ApplicationReadyEvent> {
    private final Jackson2ResourceReader resourceReader;
    private final Resource sourceData;

    public AlbumRepositoryPopulator() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        resourceReader = new Jackson2ResourceReader(mapper);
        sourceData = new ClassPathResource("albums.json");
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        CrudRepository albumRepository =
                BeanFactoryUtils.beanOfTypeIncludingAncestors(event.getApplicationContext(), CrudRepository.class);

        if (albumRepository != null && albumRepository.count() == 0) {
            populate(albumRepository);
        }
    }

    @SuppressWarnings("unchecked")
    private void populate(CrudRepository repository) {
        Object entity = getEntityFromResource(sourceData);

        if (entity instanceof Collection) {
            for (Album album : (Collection<Album>) entity) {
                if (album != null) {
                    repository.save(album);
                }
            }
        } else {
            repository.save(entity);
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
