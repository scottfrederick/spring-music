package org.cloudfoundry.samples.music.repositories.jpa;

import org.cloudfoundry.samples.music.domain.Album;
import org.cloudfoundry.samples.music.repositories.AlbumRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaAlbumRepository extends JpaRepository<Album, String>, AlbumRepository {
}
