package com.example.security.config;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import com.github.benmanes.caffeine.cache.Caffeine;

@Configuration
@EnableCaching
public class MultipleCacheManagerConfig {
  @Bean
  @Primary
  public CacheManager cacheManager() {
    CaffeineCacheManager cacheManager = new CaffeineCacheManager("userList", "user");
    Caffeine<Object, Object> c = Caffeine.newBuilder().initialCapacity(200).maximumSize(500).weakKeys().recordStats();
    cacheManager.setCaffeine(c);
    return cacheManager;
  }
}
