package org.cloudfoundry.samples.music.config.data;

import org.cloudfoundry.samples.music.domain.Album;
import org.cloudfoundry.samples.music.repositories.redis.RedisAlbumRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.Cloud;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.JacksonJsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@Profile("redis")
public class RedisConfig {

    @Autowired(required = false)
    private Cloud cloud = null;

    @Bean
    public RedisAlbumRepository redisRepository() {
        return new RedisAlbumRepository(redisTemplate());
    }

    @Bean
    public RedisTemplate<String, Album> redisTemplate() {
        RedisTemplate<String, Album> template = new RedisTemplate<String, Album>();

        template.setConnectionFactory(redisConnection());

        RedisSerializer<String> stringSerializer = new StringRedisSerializer();
        RedisSerializer<Album> albumSerializer = new JacksonJsonRedisSerializer<Album>(Album.class);

        template.setKeySerializer(stringSerializer);
        template.setValueSerializer(albumSerializer);
        template.setHashKeySerializer(stringSerializer);
        template.setHashValueSerializer(albumSerializer);

        return template;
    }

    @Bean
    public RedisConnectionFactory redisConnection() {
        if (cloud != null) {
            return cloud.getSingletonServiceConnector(RedisConnectionFactory.class, null);
        } else {
            return new JedisConnectionFactory();
        }
    }
}
