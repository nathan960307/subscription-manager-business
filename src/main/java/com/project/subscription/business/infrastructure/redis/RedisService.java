package com.project.subscription.business.infrastructure.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class RedisService {

    private final RedisTemplate<String, String> redisTemplate;

    // 저장
    public void save(String key, String value, long expireTime) {
        redisTemplate.opsForValue().set(key, value, expireTime, TimeUnit.MILLISECONDS);
    }

    // 조회
    public String get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    // 삭제
    public void delete(String key) {
        redisTemplate.delete(key);
    }

    // 존재 여부
    public boolean exists(String key) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }

    // redis 락 획득
    public boolean acquireLock(String key, long expireTimeMillis) {
        Boolean success = redisTemplate.opsForValue().setIfAbsent(key, "locked", expireTimeMillis, TimeUnit.MILLISECONDS);
        return Boolean.TRUE.equals(success);
    }

    // redis 락 해제
    public void releaseLock(String key) {
        redisTemplate.delete(key);
    }

}
