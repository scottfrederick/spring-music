package org.cloudfoundry.samples.music.repositories.redis;

import org.cloudfoundry.samples.music.domain.Album;
import org.cloudfoundry.samples.music.domain.RandomIdGenerator;

import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
@Profile("redis")
public class RedisAlbumRepository implements CrudRepository<Album, String> {
    public static final String ALBUMS_KEY = "albums";

    private final RandomIdGenerator idGenerator;
    private final HashOperations<String, String, Album> hashOps;

    public RedisAlbumRepository(RedisTemplate<String, Album> redisTemplate) {
        this.hashOps = redisTemplate.opsForHash();
        this.idGenerator = new RandomIdGenerator();
    }

    @Override
    public <S extends Album> S save(S album) {
        if (album.getId() == null) {
            album.setId(idGenerator.generateId());
        }

        hashOps.put(ALBUMS_KEY, album.getId(), album);

        return album;
    }

    @Override
    public <S extends Album> Iterable<S> saveAll(Iterable<S> albums) {
        List<S> result = new ArrayList<>();

        for (S entity : albums) {
            save(entity);
            result.add(entity);
        }

        return result;
    }

    @Override
    public Optional<Album> findById(String id) {
        return Optional.ofNullable(hashOps.get(ALBUMS_KEY, id));
    }

    @Override
    public boolean existsById(String id) {
        return hashOps.hasKey(ALBUMS_KEY, id);
    }

    @Override
    public Iterable<Album> findAll() {
        return hashOps.values(ALBUMS_KEY);
    }

    @Override
    public Iterable<Album> findAllById(Iterable<String> ids) {
        return hashOps.multiGet(ALBUMS_KEY, convertIterableToList(ids));
    }

    @Override
    public long count() {
        return hashOps.keys(ALBUMS_KEY).size();
    }

    @Override
    public void deleteById(String id) {
        hashOps.delete(ALBUMS_KEY, id);
    }

    @Override
    public void delete(Album album) {
        hashOps.delete(ALBUMS_KEY, album.getId());
    }

    @Override
    public void deleteAll(Iterable<? extends Album> albums) {
        for (Album album : albums) {
            delete(album);
        }
    }

    @Override
    public void deleteAll() {
        Set<String> ids = hashOps.keys(ALBUMS_KEY);
        for (String id : ids) {
            deleteById(id);
        }
    }

    @Override
    public void deleteAllById(Iterable<? extends String> ids) {
        for (String id : ids) {
            deleteById(id);
        }
    }

    private <T> List<T> convertIterableToList(Iterable<T> iterable) {
        List<T> list = new ArrayList<>();
        for (T object : iterable) {
            list.add(object);
        }
        return list;
    }
}
