package jpa.spring.service.serviceIml;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface BaseRedisServiceImpl {

    void set(String key, Object value);

    void setTimeToLive(String kry, long timeoutInDays);

    void hashSet(String key, String field, Object value);

    boolean hashExists(String key, String field);

    Object get(String key);

    public Map<String, Object> getFiled(String key);

    Object hashGet(String key, String filed);


    List<Object> hashGetByFieldPrefix(String key, String filedPrefix);

    Set<String> getFieldPrefixes(String key);

    void delete(String key);

    void delete(String key, String field);

    void delete(String key, List<String> files);

}
