package jpa.spring.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import jpa.spring.service.serviceIml.BaseRedisServiceImpl;

@Service
public class BaseRedisService implements BaseRedisServiceImpl {

    private final RedisTemplate<String, Object> redisTemplate;

    private final HashOperations<String, String, Object> hashOperations;

    public BaseRedisService(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
        this.hashOperations = redisTemplate.opsForHash();
    }

    @Override
    public void set(String key, Object value) {
        redisTemplate.opsForValue().set(key, value);
    }

    @Override
    public void setTimeToLive(String kry, long timeoutInDays) {
        redisTemplate.expire(kry, timeoutInDays, TimeUnit.DAYS);
    }

    @Override
    public void hashSet(String key, String field, Object value) {
        hashOperations.put(key, field, value);
    }

    @Override
    public boolean hashExists(String key, String field) {
        return hashOperations.hasKey(key, field);
    }

    @Override
    public Object get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    @Override
    public Map<String, Object> getFiled(String key) {
        return hashOperations.entries(key);
    }

    @Override
    public Object hashGet(String key, String filed) {
        return hashOperations.get(key, filed);
    }

    @Override
    public List<Object> hashGetByFieldPrefix(String key, String filedPrefix) {
        List<Object> objects = new ArrayList<>();
        Map<String, Object> hashEmtries = hashOperations.entries(key);
        for (Map.Entry<String, Object> entry : hashEmtries.entrySet()) {
            if (entry.getKey().startsWith(filedPrefix)) {
                objects.add(entry.getValue());
            }
        }
        return objects;
    }

    @Override
    public Set<String> getFieldPrefixes(String key) {
        return hashOperations.entries(key).keySet();
    }

    @Override
    public void delete(String key) {
        redisTemplate.delete(key);

    }

    @Override
    public void delete(String key, String field) {
        hashOperations.delete(key, field);

    }

    @Override
    public void delete(String key, List<String> files) {
        for (String field : files) {
            hashOperations.delete(key, field);
        }
    }
}
