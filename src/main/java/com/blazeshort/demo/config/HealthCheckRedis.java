package com.blazeshort.demo.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class HealthCheckRedis implements CommandLineRunner {
    private final RedisTemplate<String, String> redisTemplate;

    @Override
    public void run(String... args) {
        redisTemplate.opsForValue().set("health", "ok");
        System.out.println("Redis says: " + redisTemplate.opsForValue().get("health"));
    }
}
