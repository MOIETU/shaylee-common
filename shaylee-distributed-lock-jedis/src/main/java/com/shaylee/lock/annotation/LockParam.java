package com.shaylee.lock.annotation;

import java.lang.annotation.*;

/**
 * Title: 分布式锁参数，用于组建key值
 * Project: shaylee-common
 *
 * @author Adrian
 * @version 1.0
 * @date 2020-2-25
 */
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface LockParam {
    /**
     * 如果动态key在user对象中，那么就需要设置fields的值为user对象中的属性名可以为多个，基本类型则不需要设置该值
     * <p>例1：public void count(@LockKeyParam({"id"}) User user)
     * <p>例2：public void count(@LockKeyParam({"id","userName"}) User user)
     * <p>例3：public void count(@LockKeyParam String userId)
     */
    String[] fields() default {};
}
