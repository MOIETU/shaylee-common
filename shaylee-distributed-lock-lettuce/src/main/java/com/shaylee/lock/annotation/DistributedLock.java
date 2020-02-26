package com.shaylee.lock.annotation;

import java.lang.annotation.*;

/**
 * Title: 分布式锁注解
 * Project: shaylee-common
 *
 * @author Adrian
 * @version 1.0
 * @date 2020-2-25
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface DistributedLock {

    /**
     * 锁key值
     * <p>默认为""
     *
     * @return
     */
    String key() default "";

    /**
     * 锁存储的value
     * <p><strong>默认使用目标函数String,Number类型的值作为value,多个参数以&连接</strong>
     *
     * @return
     */
    String value() default "";

    /**
     * 过期时间(秒)
     *
     * @return
     */
    long expireTime() default 10 * 60L;

    /**
     * 是否支持事务
     *
     * @return
     */
    boolean isTransactional() default false;
}
