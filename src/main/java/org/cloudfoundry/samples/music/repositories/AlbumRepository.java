package org.cloudfoundry.samples.music.repositories;

import org.cloudfoundry.samples.music.domain.Album;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface AlbumRepository extends CrudRepository<Album, Long> {
}
