package org.cloudfoundry.samples.music.repositories;

import org.cloudfoundry.samples.music.domain.Album;
import org.springframework.data.repository.CrudRepository;

public interface AlbumRepository extends CrudRepository<Album, String> {
}
