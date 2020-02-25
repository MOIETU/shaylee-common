package com.shaylee.lock.aspect;

import com.shaylee.lock.annotation.DistributedLock;
import com.shaylee.lock.annotation.LockParam;
import com.shaylee.lock.utils.RedisLockUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Title: 分布式锁切面实现
 * Project: shaylee-common
 *
 * @author Adrian
 * @version 1.0
 * @date 2020-2-25
 */
@Aspect
@Component
public class DistributedLockAspect {
    private Logger logger = LoggerFactory.getLogger(getClass());

    private static final String KEY_PREFIX = "DISTRIBUTED_LOCK_";
    private static final String KEY_SEPARATOR = "@@";

    @Value("${spring.aop.proxy-target-class:#{true}}")
    private Boolean proxyTargetClass;

    @Autowired
    private RedisLockUtils redisLockUtils;

    @Pointcut(value = "@annotation(com.shaylee.lock.annotation.DistributedLock)")
    public void lockPointcut() {
        
    }

    @Around(value = "lockPointcut()")
    public Object dealLock(ProceedingJoinPoint joinPoint) throws Throwable {
        Method method;
        if (proxyTargetClass) {
            method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        } else {
            method = this.getDataSourceByClazz(joinPoint);
        }
        Object[] args = joinPoint.getArgs();
        DistributedLock distributedLock = method.getAnnotation(DistributedLock.class);
        if (distributedLock == null) {
            throw new RuntimeException("==========>locking exception: The lock parameter cannot be retrieved!!!");
        }
        // 获取Key值
        String key = buildLockKey(distributedLock, method, args);
        String value = UUID.randomUUID().toString();
        // 加锁
        boolean isLock = redisLockUtils.setNxPx(key, value, distributedLock.expireTime());
        if (!isLock) {
            logger.info("========={}========> get lock fail , key:{}",
                    this.getClass().getSimpleName(), key);
            //TODO 添加自定义异常
            throw new RuntimeException("get lock fail");
        }
        logger.info("========={}========> get lock success, key:{}",
                this.getClass().getSimpleName(), key);

        try {
            // 执行目标方法
            return joinPoint.proceed();
        } finally {
            // 释放锁
            boolean releaseResult = redisLockUtils.delByKV(key, value);
            logger.debug("========={}========> release lock : {} , releaseResult :{}",
                    this.getClass().getSimpleName(), key, releaseResult ? "success" : "fail");
        }

    }

    /**
     * 构造分布式锁的键
     *
     * @param lock   注解
     * @param method 注解标记的方法
     * @param args   方法上的参数
     * @return
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     */
    private String buildLockKey(DistributedLock lock, Method method, Object[] args) throws NoSuchFieldException, IllegalAccessException {
        StringBuilder key = new StringBuilder(KEY_SEPARATOR + KEY_PREFIX + lock.key());

        // 迭代全部参数的注解，根据使用LockKeyParam的注解的参数所在的下标，来获取args中对应下标的参数值拼接到前半部分key上
        Annotation[][] parameterAnnotations = method.getParameterAnnotations();

        for (int i = 0; i < parameterAnnotations.length; i++) {
            // 循环该参数全部注解
            for (Annotation annotation : parameterAnnotations[i]) {
                // 注解不是 @LockKeyParam
                if (!annotation.annotationType().equals(LockParam.class)) {
                    continue;
                }

                // 获取所有fields
                String[] fields = ((LockParam) annotation).fields();
                if (Objects.isNull(fields) || fields.length == 0) {
                    // 普通数据类型直接拼接
                    if (Objects.isNull(args[i]) || args[i].equals(null)) {
                        throw new RuntimeException("动态参数不能为null");
                    }
                    key.append(KEY_SEPARATOR).append(args[i]);
                } else {
                    // @LockKeyParam的fields值不为null，所以当前参数应该是对象类型
                    for (String field : fields) {
                        Class<?> clazz = args[i].getClass();
                        //获取本类及父类私有变量字段
                        while (clazz != Object.class) {
                            Field[] declaredFields = clazz.getDeclaredFields();
                            boolean isContain = Arrays.stream(declaredFields).map(Field::getName)
                                    .collect(Collectors.toList()).contains(field);
                            if (isContain) {
                                Field declaredField = clazz.getDeclaredField(field);
                                declaredField.setAccessible(true);
                                Object value = declaredField.get(args[i]);
                                key.append(KEY_SEPARATOR).append(value);
                                break;
                            }
                            clazz = clazz.getSuperclass();
                        }
                    }
                }
            }
        }
        return key.toString();
    }

    /**
     * 支持JDK动态代理
     */
    private Method getDataSourceByClazz(JoinPoint joinPoint) throws Exception {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        // JDK动态代理，代理对象的方法不会有注解，要取注解只能从目标对象取
        Class<?> clazz = joinPoint.getTarget().getClass();
        return clazz.getDeclaredMethod(methodSignature.getName(), method.getParameterTypes());
    }
}
