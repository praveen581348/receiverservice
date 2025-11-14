package com.example.receiverservice.cache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class MessageCacheService {

    @Autowired
    private StringRedisTemplate redisTemplate;

    public void cacheMessage(String id, String content) {
        redisTemplate.opsForValue().set("kafka:message:" + id, content);
    }    

    public String getMessage(String id) {
        return redisTemplate.opsForValue().get("message:" + id);
    }
}
