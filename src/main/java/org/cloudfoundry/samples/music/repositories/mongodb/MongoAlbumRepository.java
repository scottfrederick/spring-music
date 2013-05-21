package org.cloudfoundry.samples.music.repositories.mongodb;

import org.cloudfoundry.samples.music.domain.Album;
import org.cloudfoundry.samples.music.repositories.AlbumRepository;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MongoAlbumRepository extends MongoRepository<Album, String>, AlbumRepository {
}