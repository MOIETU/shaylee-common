package com.shaylee.redis.service.impl;

import com.shaylee.redis.constant.CacheConstant;
import com.shaylee.redis.service.CacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Title: Redis缓存服务实现
 * Project: shaylee-common
 *
 * @author Adrian
 * @date 2020-03-01
 */
@Component
public class RedisServiceImpl implements CacheService {
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public void set(String key, Object value, long expire){
        redisTemplate.opsForValue().set(key, value);
        if(expire != CacheConstant.NOT_EXPIRE){
            expire(key, expire);
        }
    }

    @Override
    public void set(String key, Object value){
        set(key, value, CacheConstant.DEFAULT_EXPIRE);
    }

    @Override
    public Object get(String key, long expire) {
        Object value = redisTemplate.opsForValue().get(key);
        if(expire != CacheConstant.NOT_EXPIRE){
            expire(key, expire);
        }
        return value;
    }

    @Override
    public Object get(String key) {
        return get(key, CacheConstant.NOT_EXPIRE);
    }

    @Override
    public void delete(String key) {
        redisTemplate.delete(key);
    }

    @Override
    public void delete(Collection<String> keys) {
        redisTemplate.delete(keys);
    }

    @Override
    public Object hGet(String key, String field) {
        return redisTemplate.opsForHash().get(key, field);
    }

    @Override
    public Map<String, Object> hGetAll(String key){
        HashOperations<String, String, Object> hashOperations = redisTemplate.opsForHash();
        return hashOperations.entries(key);
    }

    @Override
    public void hMSet(String key, Map<String, Object> map){
        hMSet(key, map, CacheConstant.DEFAULT_EXPIRE);
    }

    @Override
    public void hMSet(String key, Map<String, Object> map, long expire){
        redisTemplate.opsForHash().putAll(key, map);

        if(expire != CacheConstant.NOT_EXPIRE){
            expire(key, expire);
        }
    }

    @Override
    public void hSet(String key, String field, Object value) {
        hSet(key, field, value, CacheConstant.DEFAULT_EXPIRE);
    }

    @Override
    public void hSet(String key, String field, Object value, long expire) {
        redisTemplate.opsForHash().put(key, field, value);

        if(expire != CacheConstant.NOT_EXPIRE){
            expire(key, expire);
        }
    }

    @Override
    public void expire(String key, long expire){
        redisTemplate.expire(key, expire, TimeUnit.SECONDS);
    }

    @Override
    public void hDel(String key, Object... fields){
        redisTemplate.opsForHash().delete(key, fields);
    }

    @Override
    public void leftPush(String key, Object value){
        leftPush(key, value, CacheConstant.DEFAULT_EXPIRE);
    }

    @Override
    public void leftPush(String key, Object value, long expire){
        redisTemplate.opsForList().leftPush(key, value);

        if(expire != CacheConstant.NOT_EXPIRE){
            expire(key, expire);
        }
    }

    @Override
    public Object rightPop(String key){
        return redisTemplate.opsForList().rightPop(key);
    }
}
