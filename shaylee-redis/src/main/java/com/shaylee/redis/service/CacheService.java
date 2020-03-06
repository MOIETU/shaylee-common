package com.shaylee.redis.service;

import java.util.Collection;
import java.util.Map;

/**
 * Title: 缓存服务类
 * Project: shaylee-common
 *
 * @author Adrian
 * @date 2020-03-01
 */
public interface CacheService {

    void set(String key, Object value, long expire);

    void set(String key, Object value);

    Object get(String key, long expire);

    Object get(String key);

    void delete(String key);

    void delete(Collection<String> keys);

    Object hGet(String key, String field);

    Map<String, Object> hGetAll(String key);

    void hMSet(String key, Map<String, Object> map);

    void hMSet(String key, Map<String, Object> map, long expire);

    void hSet(String key, String field, Object value);

    void hSet(String key, String field, Object value, long expire);

    void expire(String key, long expire);

    void hDel(String key, Object... fields);

    void leftPush(String key, Object value);

    void leftPush(String key, Object value, long expire);

    Object rightPop(String key);
}
