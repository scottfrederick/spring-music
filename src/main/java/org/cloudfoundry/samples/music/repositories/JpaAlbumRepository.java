package org.cloudfoundry.samples.music.repositories;

import org.cloudfoundry.samples.music.domain.Album;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaAlbumRepository extends JpaRepository<Album, Long>, AlbumRepository {
}
