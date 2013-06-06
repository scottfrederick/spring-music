package org.cloudfoundry.samples.music.repositories.redis;

import org.cloudfoundry.samples.music.domain.Album;
import org.cloudfoundry.samples.music.domain.RandomIdGenerator;
import org.cloudfoundry.samples.music.repositories.AlbumRepository;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class RedisAlbumRepository implements AlbumRepository {
    public static final String ALBUMS_KEY = "albums";

    private final RandomIdGenerator idGenerator;
    private final HashOperations<String, String, String> hashOps;
    private ObjectMapper objectMapper;

    public RedisAlbumRepository(StringRedisTemplate redisTemplate) {
        this.hashOps = redisTemplate.opsForHash();

        this.idGenerator = new RandomIdGenerator();
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public <S extends Album> S save(S entity) {
        if (entity.getId() == null) {
            entity.setId(idGenerator.generateId());
        }

        try {
            StringWriter stringWriter = new StringWriter();
            objectMapper.writeValue(stringWriter, entity);
            hashOps.put(ALBUMS_KEY, entity.getId(), stringWriter.toString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return entity;
    }

    @Override
    public <S extends Album> Iterable<S> save(Iterable<S> entities) {
        List<S> result = new ArrayList<S>();

        for (S entity : entities) {
            save(entity);
            result.add(entity);
        }

        return result;
    }

    @Override
    public Album findOne(String s) {
        String stringAlbum = hashOps.get(ALBUMS_KEY, s);
        try {
            return objectMapper.readValue(stringAlbum, Album.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean exists(String s) {
        return findOne(s) != null;
    }

    @Override
    public Iterable<Album> findAll() {
        Map<String, String> entries = hashOps.entries(ALBUMS_KEY);

        return createAlbumsFromStrings(entries.values());
    }

    @Override
    public Iterable<Album> findAll(Iterable<String> strings) {
        List<String> keys = new ArrayList<String>();
        for (String string : strings) {
            keys.add(string);
        }

        return createAlbumsFromStrings(keys);
    }

    private List<Album> createAlbumsFromStrings(Collection<String> values) {
        List<Album> albums = new ArrayList<Album>();
        for (String value : values) {
            try {
                albums.add(objectMapper.readValue(value, Album.class));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return albums;
    }

    @Override
    public long count() {
        return hashOps.entries(ALBUMS_KEY).size();
    }

    @Override
    public void delete(String s) {
        hashOps.delete(ALBUMS_KEY, s);
    }

    @Override
    public void delete(Album entity) {
        hashOps.delete(ALBUMS_KEY, entity.getId());
    }

    @Override
    public void delete(Iterable<? extends Album> entities) {
        for (Album album : entities) {
            hashOps.delete(ALBUMS_KEY, album.getId());
        }
    }

    @Override
    public void deleteAll() {
        Map<String, String> entries = hashOps.entries(ALBUMS_KEY);
        for (String entry : entries.keySet()) {
            hashOps.delete(ALBUMS_KEY, entry);
        }
    }
}
