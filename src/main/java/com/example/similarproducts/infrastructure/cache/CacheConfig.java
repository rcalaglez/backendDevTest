package com.example.similarproducts.infrastructure.cache;

import com.example.similarproducts.domain.model.ProductDetail;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class CacheConfig {

    @Bean
    public Cache<String, ProductDetail> productCache(
            @Value("${cache.ttl:60s}") Duration ttl,
            @Value("${cache.max-size:100}") int maxSize) {
        return Caffeine.newBuilder()
                .expireAfterWrite(ttl)
                .maximumSize(maxSize)
                .build();
    }
}
