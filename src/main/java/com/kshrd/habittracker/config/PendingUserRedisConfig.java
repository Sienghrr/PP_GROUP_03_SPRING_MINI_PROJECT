package com.kshrd.habittracker.config;

import com.kshrd.habittracker.dto.PendingUserRegistration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class PendingUserRedisConfig {

    @Bean
    public RedisTemplate<String, PendingUserRegistration> pendingUserRedisTemplate(
            RedisConnectionFactory connectionFactory
    ) {
        RedisTemplate<String, PendingUserRegistration> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);

        StringRedisSerializer keySerializer = new StringRedisSerializer();
        JdkSerializationRedisSerializer valueSerializer = new JdkSerializationRedisSerializer();

        template.setKeySerializer(keySerializer);
        template.setValueSerializer(valueSerializer);
        template.setHashKeySerializer(keySerializer);
        template.setHashValueSerializer(valueSerializer);
        template.afterPropertiesSet();

        return template;
    }
}