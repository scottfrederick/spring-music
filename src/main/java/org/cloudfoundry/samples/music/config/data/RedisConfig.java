package org.cloudfoundry.samples.music.config.data;

import org.cloudfoundry.runtime.env.CloudEnvironment;
import org.cloudfoundry.runtime.env.RedisServiceInfo;
import org.cloudfoundry.runtime.service.keyvalue.RedisServiceCreator;
import org.cloudfoundry.samples.music.domain.Album;
import org.cloudfoundry.samples.music.repositories.redis.RedisAlbumRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.JacksonJsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.util.List;

@Configuration
@Profile("redis")
public class RedisConfig {
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
        CloudEnvironment cloudEnvironment = new CloudEnvironment();

        if (cloudEnvironment.isCloudFoundry()) {
            List<RedisServiceInfo> serviceInfo = cloudEnvironment.getServiceInfos(RedisServiceInfo.class);
            RedisServiceCreator serviceCreator = new RedisServiceCreator();
            return serviceCreator.createService(serviceInfo.get(0));
        } else {
            return new JedisConnectionFactory();
        }
    }
}
