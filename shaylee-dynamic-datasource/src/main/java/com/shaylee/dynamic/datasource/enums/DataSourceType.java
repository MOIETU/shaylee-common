package com.shaylee.dynamic.datasource.enums;

/**
 * Title: 数据源枚举类
 * Project: shaylee-common
 *
 * @author Adrian
 * @version 1.0
 * @date 2020-2-20
 */
public enum DataSourceType {
    /**
     * API主库
     */
    MASTER_API("master.api"),
    /**
     * API从库
     */
    SLAVE_API("slave.api");

    private final String name;

    DataSourceType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}