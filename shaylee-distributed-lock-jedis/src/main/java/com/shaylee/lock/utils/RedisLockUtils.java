package com.shaylee.lock.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisCommands;

import java.util.ArrayList;
import java.util.List;

/**
 * Title: Redis实现分布式锁关键指令封装
 * Project: shaylee-common
 *
 * @author Adrian
 * @date 2020-02-25
 */
@Component
public class RedisLockUtils {
    private static final Logger logger = LoggerFactory.getLogger(RedisLockUtils.class);

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 表示只有当锁定资源不存在的时候才能 SET 成功。利用 Redis 的原子性，
     * 保证了只有第一个请求的线程才能获得锁，而之后的所有线程在锁定资源被释放之前都不能获得锁。
     */
    private static final String SET_IF_NOT_EXIST = "NX";

    /**
     * expire 表示锁定的资源的自动过期时间，单位是毫秒。具体过期时间根据实际场景而定。
     */
    private static final String SET_WITH_EXPIRE_TIME = "PX";

    /**
     * 以秒为单位设置key的过期时间，等效于EXPIRE key seconds
     */
    private static final String SET_WITH_EXPIRE_TIME_E = "EX";

    private static final String LOCK_SUCCESS = "OK";

    /**
     * 通过 Lua 脚本来达到释放锁的原子操作
     */
    public static final String UNLOCK_LUA = "if redis.call(\"get\",KEYS[1]) == ARGV[1] then return redis.call(\"del\",KEYS[1]) else return 0 end";

    /**
     * SetNx + Expire
     *
     * @param key
     * @param value
     * @param expire
     * @return
     */
    public boolean setNxPx(String key, String value, Long expire) {
        try {
            RedisCallback<String> callback = (connection) -> {
                JedisCommands commands = (JedisCommands) connection.getNativeConnection();
                return commands.set(key, value, SET_IF_NOT_EXIST, SET_WITH_EXPIRE_TIME_E, expire);
            };
            String result = redisTemplate.execute(callback);
            return LOCK_SUCCESS.equals(result);
        } catch (Exception e) {
            logger.error("set redis occured an exception", e);
        }
        return false;
    }

    /**
     * 根据Key-Value删除
     *
     * @param key
     * @param value
     * @return
     */
    public boolean delByKV(String key, String value) {
        List<String> keys = new ArrayList<>();
        keys.add(key);
        List<String> args = new ArrayList<>();
        args.add(value);
        try {
            // 使用lua脚本删除redis中匹配value的key，可以避免由于方法执行时间过长而redis锁自动过期失效的时候误删其他线程的锁
            // spring自带的执行脚本方法中，集群模式直接抛出不支持执行脚本的异常，所以只能拿到原redis的connection来执行脚本
            RedisCallback<Long> callback = (connection) -> {
                Object nativeConnection = connection.getNativeConnection();
                // 集群模式和单机模式虽然执行脚本的方法一样，但是没有共同的接口，所以只能分开执行
                if (nativeConnection instanceof JedisCluster) {// 集群模式
                    return (Long) ((JedisCluster) nativeConnection).eval(UNLOCK_LUA, keys, args);
                } else if (nativeConnection instanceof Jedis) {// 单机模式
                    return (Long) ((Jedis) nativeConnection).eval(UNLOCK_LUA, keys, args);
                }
                return 0L;
            };
            Long result = redisTemplate.execute(callback);
            return result != null && result > 0;

        } catch (Exception e) {
            logger.error("release lock occured an exception", e);
        }
        return false;
    }
}
