package com.hometask.dkp.hsbctransactionmanagement.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

/**
 * @author: dkp
 * @date: 2025-03-09
 */

@Configuration
@EnableCaching
public class CacheConfig {
    @Bean
    public CacheManager caffeineCacheManager() throws Exception {

        CaffeineCacheManager cacheManager = new CaffeineCacheManager("transactionInfo");
        cacheManager.setCaffeine(caffeineCacheBuilder());
        return cacheManager;
    }

    private Caffeine<Object, Object> caffeineCacheBuilder() {
        return Caffeine.newBuilder()
                .expireAfterWrite(20, TimeUnit.MINUTES);
    }
}
