package org.cloudfoundry.samples.music.config.data;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;

@Configuration
@Profile("redis-local")
public class RedisLocalConfig {

    @Bean
    public RedisConnectionFactory redisConnection() {
        return new JedisConnectionFactory();
    }

}
