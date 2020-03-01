package com.shaylee.lock.utils;

import io.lettuce.core.RedisFuture;
import io.lettuce.core.ScriptOutputType;
import io.lettuce.core.SetArgs;
import io.lettuce.core.api.async.RedisAsyncCommands;
import io.lettuce.core.api.async.RedisScriptingAsyncCommands;
import io.lettuce.core.cluster.api.async.RedisAdvancedClusterAsyncCommands;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutionException;

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
     * 通过 Lua 脚本来达到释放锁的原子操作
     */
    public static final String UNLOCK_LUA = "if redis.call(\"get\",KEYS[1]) == ARGV[1] then return redis.call(\"del\",KEYS[1]) else return 0 end";

    /**
     * SetNx + Expire
     *
     * @param key         key值
     * @param value       value值
     * @param expiredTime 过期时间（秒）
     * @return 设置结果
     */
    @SuppressWarnings("unchecked")
    public Boolean setNxPx(String key, String value, Long expiredTime) {
        Boolean resultBoolean = null;
        try {
            resultBoolean = redisTemplate.execute((RedisCallback<Boolean>) connection -> {
                Object nativeConnection = connection.getNativeConnection();
                String redisResult = "";
                RedisSerializer<String> stringRedisSerializer = (RedisSerializer<String>) redisTemplate.getKeySerializer();
                // lettuce连接包下序列化键值，否则无法用默认的ByteArrayCodec解析
                byte[] keyByte = stringRedisSerializer.serialize(key);
                byte[] valueByte = stringRedisSerializer.serialize(value);
                // lettuce连接包下 redis 单机模式setnx
                if (nativeConnection instanceof RedisAsyncCommands) {
                    RedisAsyncCommands asyncCommands = (RedisAsyncCommands) nativeConnection;
                    //同步方法执行，setnx禁止异步
                    redisResult = asyncCommands
                            .getStatefulConnection()
                            .sync()
                            .set(keyByte, valueByte, SetArgs.Builder.nx().ex(expiredTime));
                }
                // lettuce连接包下 redis 集群模式setnx
                if (nativeConnection instanceof RedisAdvancedClusterAsyncCommands) {
                    RedisAdvancedClusterAsyncCommands clusterAsyncCommands = (RedisAdvancedClusterAsyncCommands) nativeConnection;
                    redisResult = clusterAsyncCommands
                            .getStatefulConnection()
                            .sync()
                            .set(keyByte, valueByte, SetArgs.Builder.nx().ex(expiredTime));
                }
                //返回加锁结果
                return "OK".equalsIgnoreCase(redisResult);
            });
        } catch (Exception e) {
            logger.error("set key value NX PX exception", e);
        }
        return resultBoolean != null && resultBoolean;
    }

    /**
     * 根据Key-Value删除
     *
     * @param key   键
     * @param value 值
     * @return 删除结果
     */
    @SuppressWarnings("unchecked")
    public Boolean delByKV(String key, String value) {
        try {
            if (value == null) {
                return false;
            }

            RedisSerializer<String> stringRedisSerializer = (RedisSerializer<String>) redisTemplate.getKeySerializer();
            // lettuce连接包下序列化键值，否则无法用默认的ByteArrayCodec解析
            Object[] keyByte = new Object[]{stringRedisSerializer.serialize(key)};
            byte[] valueByte = stringRedisSerializer.serialize(value);

            Long result = redisTemplate.execute((RedisCallback<Long>) connection -> {
                Object nativeConnection = connection.getNativeConnection();
                if (nativeConnection instanceof RedisScriptingAsyncCommands) {
                    RedisScriptingAsyncCommands<Object, byte[]> scriptingAsyncCommands = (RedisScriptingAsyncCommands<Object, byte[]>) nativeConnection;
                    RedisFuture future = scriptingAsyncCommands.eval(UNLOCK_LUA, ScriptOutputType.INTEGER, keyByte, valueByte);
                    try {
                        Object o = future.get();
                        return (Long) o;
                    } catch (InterruptedException | ExecutionException e) {
                        logger.error("Future get failed.", e);
                        return 0L;
                    }
                } else {
                    logger.warn("Failed to convert nativeConnection. Is your SpringBoot main version > 2.0 ? Only lib:lettuce is supported.");
                    return 0L;
                }
            });
            return result != null && result > 0;
        } catch (Exception e) {
            logger.error("del key value exception", e);
        }
        return false;
    }
}
