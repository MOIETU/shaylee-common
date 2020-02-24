package com.shaylee.dynamic.datasource.aspectj;

import com.shaylee.dynamic.datasource.annotation.DataSource;
import com.shaylee.dynamic.datasource.enums.DataSourceType;
import com.shaylee.dynamic.datasource.utils.DynamicDataSourceContextHolder;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * Title: 多数据源切面处理类
 * Project: shaylee-common
 *
 * @author Adrian
 * @version 1.0
 * @date 2020-2-20
 */
@Aspect
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class DataSourceAspect {
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Value("${spring.aop.proxy-target-class:#{true}}")
    private Boolean proxyTargetClass;

    @Pointcut("@annotation(com.shaylee.dynamic.datasource.annotation.DataSource) " +
            "|| @within(com.shaylee.dynamic.datasource.annotation.DataSource)")
    public void dataSourcePointCut() {

    }

    @Around("dataSourcePointCut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        // 多数据源上下文设为需要切换的数据源
        DataSourceType dataSourceType = this.getDataSourceType(point);
        DynamicDataSourceContextHolder.setDataSourceType(dataSourceType);
        logger.debug("=====>thread:{} switch to datasource [{}]", Thread.currentThread().getName(), dataSourceType);
        try {
            return point.proceed();
        } finally {
            DynamicDataSourceContextHolder.clearDataSourceType();
            logger.debug("=====>thread:{} clean datasource", Thread.currentThread().getName());
        }
    }

    /**
     * 获取需要切换的数据源
     */
    private DataSourceType getDataSourceType(ProceedingJoinPoint point) throws Exception {
        // 支持类和方法上使用
        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method;
        if (proxyTargetClass) {
            method = signature.getMethod();
        } else {
            method = this.getDataSourceByClazz(point);
        }
        DataSource methodDataSource = method.getAnnotation(DataSource.class);
        // 方法级别优先级更高
        if (methodDataSource != null) {
            return methodDataSource.value();
        } else {
            Class<?> targetClass = point.getTarget().getClass();
            DataSource targetDataSource = targetClass.getAnnotation(DataSource.class);
            return targetDataSource.value();
        }
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
