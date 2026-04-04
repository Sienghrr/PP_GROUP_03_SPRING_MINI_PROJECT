package com.kshrd.habittracker.config;

import com.kshrd.habittracker.utils.UUIDTypeHandler;
import org.mybatis.spring.boot.autoconfigure.ConfigurationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.UUID;

@Configuration
public class MyBatisConfig {
    @Bean
    public ConfigurationCustomizer configurationCustomizer (){
        return configuration -> {
            configuration.getTypeHandlerRegistry().register(
                    UUID.class,
                    UUIDTypeHandler.class
            );
        };
    }
}
