package com.GateKeeper.gateKeeper.config;

import com.GateKeeper.gateKeeper.domain.Visit;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.auditing.DateTimeProvider;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.GenericToStringSerializer;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.Optional;

@Configuration
@EntityScan("com.GateKeeper.gateKeeper.domain")
@EnableJpaRepositories("com.GateKeeper.gateKeeper.repos")
@EnableTransactionManagement
@EnableJpaAuditing(dateTimeProviderRef = "auditingDateTimeProvider")
public class DomainConfig {
//    @Bean
//    public RedisTemplate<String, Visit> getRedisVisitTemplate(RedisConnectionFactory redisConnectionFactory){
//        RedisTemplate<String, Visit> redisVisitTemplate = new RedisTemplate<>();
//        redisVisitTemplate.setConnectionFactory(redisConnectionFactory);
//        redisVisitTemplate.setValueSerializer(new JdkSerializationRedisSerializer());
//        return redisVisitTemplate;
//    }

    @Bean(name = "auditingDateTimeProvider")
    public DateTimeProvider dateTimeProvider() {
        return () -> Optional.of(OffsetDateTime.now());
    }

}
