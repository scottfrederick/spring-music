package org.cloudfoundry.samples.music.repositories.redis;

import org.cloudfoundry.samples.music.domain.Album;
import org.cloudfoundry.samples.music.domain.RandomIdGenerator;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.repository.CrudRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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
    public <S extends Album> Iterable<S> save(Iterable<S> albums) {
        List<S> result = new ArrayList<>();

        for (S entity : albums) {
            save(entity);
            result.add(entity);
        }

        return result;
    }

    @Override
    public Album findOne(String id) {
        return hashOps.get(ALBUMS_KEY, id);
    }

    @Override
    public boolean exists(String id) {
        return hashOps.hasKey(ALBUMS_KEY, id);
    }

    @Override
    public Iterable<Album> findAll() {
        return hashOps.values(ALBUMS_KEY);
    }

    @Override
    public Iterable<Album> findAll(Iterable<String> ids) {
        return hashOps.multiGet(ALBUMS_KEY, convertIterableToList(ids));
    }

    @Override
    public long count() {
        return hashOps.keys(ALBUMS_KEY).size();
    }

    @Override
    public void delete(String id) {
        hashOps.delete(ALBUMS_KEY, id);
    }

    @Override
    public void delete(Album album) {
        hashOps.delete(ALBUMS_KEY, album.getId());
    }

    @Override
    public void delete(Iterable<? extends Album> albums) {
        for (Album album : albums) {
            delete(album);
        }
    }

    @Override
    public void deleteAll() {
        Set<String> ids = hashOps.keys(ALBUMS_KEY);
        for (String id : ids) {
            delete(id);
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
