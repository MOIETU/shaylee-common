package com.shaylee.dynamic.datasource.properties;

import lombok.Data;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Title: 数据源配置文件映射类
 * Project: shaylee-common
 *
 * @author Adrian
 * @date 2020-02-20
 */
@Data
public class DataSourceProperties {
    public static final String MASTER_PREFIX = "master.";
    public static final String SLAVE_PREFIX = "slave.";
    /**
     * JDBC连接驱动
     */
    private String driverClassName;
    /**
     * 默认数据源
     */
    private String defaultDatasource;
    /**
     * 主库JDBC连接参数
     */
    private Map<String, JdbcConnectionProperties> master = new LinkedHashMap<>();
    /**
     * 从库JDBC连接参数
     */
    private Map<String, JdbcConnectionProperties> slave = new LinkedHashMap<>();
    /**
     * Druid默认参数
     */
    private int initialSize = 10;
    private int maxActive = 100;
    private int minIdle = 10;
    private long maxWait = 60000L;
    private boolean poolPreparedStatements = true;
    private int maxPoolPreparedStatementPerConnectionSize = 20;
    private long timeBetweenEvictionRunsMillis = 60000L;
    private long minEvictableIdleTimeMillis = 300000L;
    private String validationQuery = "SELECT 1 FROM DUAL";
    private boolean testWhileIdle = true;
    private boolean testOnBorrow = false;
    private boolean testOnReturn = false;
    private long maxEvictableIdleTimeMillis = 25200000L;
    private int validationQueryTimeout = -1;
    private int maxOpenPreparedStatements = -1;
    private boolean sharePreparedStatements = false;
    private String filters = "stat,wall";

    public Map<String, JdbcConnectionProperties> getMaster() {
        Map<String, JdbcConnectionProperties> jdbcConnectionPropertiesMap = new LinkedHashMap<>(master.size());
        master.forEach((k, v) -> jdbcConnectionPropertiesMap.put(MASTER_PREFIX + k, v));
        return jdbcConnectionPropertiesMap;
    }

    public Map<String, JdbcConnectionProperties> getSlave() {
        Map<String, JdbcConnectionProperties> jdbcConnectionPropertiesMap = new LinkedHashMap<>(slave.size());
        slave.forEach((k, v) -> jdbcConnectionPropertiesMap.put(SLAVE_PREFIX + k, v));
        return jdbcConnectionPropertiesMap;
    }
}