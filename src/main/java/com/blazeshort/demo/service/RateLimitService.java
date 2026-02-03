package com.blazeshort.demo.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class RateLimitService {
    private final StringRedisTemplate stringRedisTemplate;
    private final RedisTemplate<String, String> redisTemplate;

    public boolean isAllowed(String key, int limit, int windowSearch){
        Long count = redisTemplate.opsForValue().increment(key);
        if(count == 1){
           redisTemplate.expire(key, Duration.ofSeconds(windowSearch));
        }
        return count <= limit;
    }

}
