package org.cloudfoundry.samples.music.config.data;

import org.cloudfoundry.samples.music.repositories.redis.RedisAlbumRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;

@Configuration
@Profile("redis")
public class RedisConfig {
    @Bean
    public RedisAlbumRepository redisRepository() {
        return new RedisAlbumRepository(redisTemplate());
    }

    @Bean
    public StringRedisTemplate redisTemplate() {
        return new StringRedisTemplate(redisConnection());
    }

    @Bean
    public RedisConnectionFactory redisConnection() {
        JedisConnectionFactory connectionFactory = new JedisConnectionFactory();
        // connectionFactory.setHostName("");
        // connectionFactory.setPort(0);
        // connectionFactory.setPassword("");
        return connectionFactory;
    }
}
