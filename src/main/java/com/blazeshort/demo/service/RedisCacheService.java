package com.blazeshort.demo.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class RedisCacheService {

    private final RedisTemplate<String, String> redisTemplate;

    private static final String URL_KEY_PREFIX= "short_url:";

    public void add(String code, String originalUrl, Duration ttl){
        redisTemplate.opsForValue().set(URL_KEY_PREFIX+code,originalUrl,ttl);
    }

    public String get(String code){
        System.out.println(URL_KEY_PREFIX+code);
        return redisTemplate.opsForValue().get(URL_KEY_PREFIX+code);
    }

    public void remove(String code){
        redisTemplate.delete(URL_KEY_PREFIX+code);
    }
}
