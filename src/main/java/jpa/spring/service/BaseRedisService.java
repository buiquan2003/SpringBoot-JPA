package jpa.spring.service;

import jpa.spring.service.serviceIml.BaseRedisServiceImpl;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class BaseRedisService implements BaseRedisServiceImpl {

    @Override
    public void set(String key, Object value) {

    }

    @Override
    public void setTimeToLive(String kry, long timeoutInDays) {

    }

    @Override
    public void hashSet(String key, String field, Object value) {

    }

    @Override
    public boolean hashExists(String key, String field) {
        return false;
    }

    @Override
    public Object get(String key) {
        return null;
    }

    @Override
    public Map<String, Object> getFiled(String key) {
        return Map.of();
    }

    @Override
    public Object hashGet(String key, String filed) {
        return null;
    }

    @Override
    public List<Object> hashGetByFieldPrefix(String key, String filedPrefix) {
        return List.of();
    }

    @Override
    public Set<String> getFieldPrefixes(String key) {
        return Set.of();
    }

    @Override
    public void delete(String key) {

    }

    @Override
    public void delete(String key, String field) {

    }

    @Override
    public void delete(String key, List<String> files) {

    }
}
