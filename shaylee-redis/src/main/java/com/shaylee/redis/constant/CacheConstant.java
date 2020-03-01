package com.shaylee.redis.constant;

import com.shaylee.core.constant.BaseConstant;

/**
 * Title: 缓存常量类
 * Project: shaylee-common
 *
 * @author Adrian
 * @date 2020-03-01
 */
public interface CacheConstant extends BaseConstant {
    /**  默认过期时长为24小时，单位：秒 */
    long DEFAULT_EXPIRE = 60 * 60 * 24L;
    /**  过期时长为1小时，单位：秒 */
    long HOUR_ONE_EXPIRE = 60 * 60 * 1L;
    /**  过期时长为6小时，单位：秒 */
    long HOUR_SIX_EXPIRE = 60 * 60 * 6L;
    /**  不设置过期时长 */
    long NOT_EXPIRE = -1L;
}
