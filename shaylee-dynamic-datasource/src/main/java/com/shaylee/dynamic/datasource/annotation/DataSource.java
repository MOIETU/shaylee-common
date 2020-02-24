package com.shaylee.dynamic.datasource.annotation;

import com.shaylee.dynamic.datasource.enums.DataSourceType;

import java.lang.annotation.*;

/**
 * Title: 多数据源注解
 * Project: shaylee-common
 *
 * @author Adrian
 * @version 1.0
 * @date 2020-2-20
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface DataSource {
    DataSourceType value() default DataSourceType.MASTER_API;
}