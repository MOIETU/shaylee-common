package com.shaylee.redis.constant;

/**
 * Title: 缓存常量类
 * Project: shaylee-common
 *
 * @author Adrian
 * @date 2020-03-01
 */
public interface CacheConstant {
    /**
     * 默认过期时长为24小时，单位：秒
     */
    Long DEFAULT_EXPIRE = 60 * 60 * 24L;
    /**
     * 过期时长为1小时，单位：秒
     */
    Long HOUR_ONE_EXPIRE = 60 * 60 * 1L;
    /**
     * 过期时长为6小时，单位：秒
     */
    Long HOUR_SIX_EXPIRE = 60 * 60 * 6L;
    /**
     * 不设置过期时长
     */
    Long NOT_EXPIRE = -1L;
}
