package com.shaylee.dynamic.datasource.properties;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Title: 数据源配置文件映射类
 * Project: shaylee-common
 *
 * @author Adrian
 * @version 1.0
 * @date 2020-2-20
 */
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

    public String getDriverClassName() {
        return driverClassName;
    }

    public void setDriverClassName(String driverClassName) {
        this.driverClassName = driverClassName;
    }

    public String getDefaultDatasource() {
        return defaultDatasource;
    }

    public void setDefaultDatasource(String defaultDatasource) {
        this.defaultDatasource = defaultDatasource;
    }

    public Map<String, JdbcConnectionProperties> getMaster() {
        Map<String, JdbcConnectionProperties> jdbcConnectionPropertiesMap = new LinkedHashMap<>(master.size());
        master.forEach((k, v) -> jdbcConnectionPropertiesMap.put(MASTER_PREFIX + k, v));
        return jdbcConnectionPropertiesMap;
    }

    public void setMaster(Map<String, JdbcConnectionProperties> master) {
        this.master = master;
    }

    public Map<String, JdbcConnectionProperties> getSlave() {
        Map<String, JdbcConnectionProperties> jdbcConnectionPropertiesMap = new LinkedHashMap<>(slave.size());
        slave.forEach((k, v) -> jdbcConnectionPropertiesMap.put(SLAVE_PREFIX + k, v));
        return jdbcConnectionPropertiesMap;
    }

    public void setSlave(Map<String, JdbcConnectionProperties> slave) {
        this.slave = slave;
    }

    public int getInitialSize() {
        return initialSize;
    }

    public void setInitialSize(int initialSize) {
        this.initialSize = initialSize;
    }

    public int getMaxActive() {
        return maxActive;
    }

    public void setMaxActive(int maxActive) {
        this.maxActive = maxActive;
    }

    public int getMinIdle() {
        return minIdle;
    }

    public void setMinIdle(int minIdle) {
        this.minIdle = minIdle;
    }

    public long getMaxWait() {
        return maxWait;
    }

    public void setMaxWait(long maxWait) {
        this.maxWait = maxWait;
    }

    public boolean isPoolPreparedStatements() {
        return poolPreparedStatements;
    }

    public void setPoolPreparedStatements(boolean poolPreparedStatements) {
        this.poolPreparedStatements = poolPreparedStatements;
    }

    public int getMaxPoolPreparedStatementPerConnectionSize() {
        return maxPoolPreparedStatementPerConnectionSize;
    }

    public void setMaxPoolPreparedStatementPerConnectionSize(int maxPoolPreparedStatementPerConnectionSize) {
        this.maxPoolPreparedStatementPerConnectionSize = maxPoolPreparedStatementPerConnectionSize;
    }

    public long getTimeBetweenEvictionRunsMillis() {
        return timeBetweenEvictionRunsMillis;
    }

    public void setTimeBetweenEvictionRunsMillis(long timeBetweenEvictionRunsMillis) {
        this.timeBetweenEvictionRunsMillis = timeBetweenEvictionRunsMillis;
    }

    public long getMinEvictableIdleTimeMillis() {
        return minEvictableIdleTimeMillis;
    }

    public void setMinEvictableIdleTimeMillis(long minEvictableIdleTimeMillis) {
        this.minEvictableIdleTimeMillis = minEvictableIdleTimeMillis;
    }

    public String getValidationQuery() {
        return validationQuery;
    }

    public void setValidationQuery(String validationQuery) {
        this.validationQuery = validationQuery;
    }

    public boolean isTestWhileIdle() {
        return testWhileIdle;
    }

    public void setTestWhileIdle(boolean testWhileIdle) {
        this.testWhileIdle = testWhileIdle;
    }

    public boolean isTestOnBorrow() {
        return testOnBorrow;
    }

    public void setTestOnBorrow(boolean testOnBorrow) {
        this.testOnBorrow = testOnBorrow;
    }

    public boolean isTestOnReturn() {
        return testOnReturn;
    }

    public void setTestOnReturn(boolean testOnReturn) {
        this.testOnReturn = testOnReturn;
    }

    public long getMaxEvictableIdleTimeMillis() {
        return maxEvictableIdleTimeMillis;
    }

    public void setMaxEvictableIdleTimeMillis(long maxEvictableIdleTimeMillis) {
        this.maxEvictableIdleTimeMillis = maxEvictableIdleTimeMillis;
    }

    public int getValidationQueryTimeout() {
        return validationQueryTimeout;
    }

    public void setValidationQueryTimeout(int validationQueryTimeout) {
        this.validationQueryTimeout = validationQueryTimeout;
    }

    public int getMaxOpenPreparedStatements() {
        return maxOpenPreparedStatements;
    }

    public void setMaxOpenPreparedStatements(int maxOpenPreparedStatements) {
        this.maxOpenPreparedStatements = maxOpenPreparedStatements;
    }

    public boolean isSharePreparedStatements() {
        return sharePreparedStatements;
    }

    public void setSharePreparedStatements(boolean sharePreparedStatements) {
        this.sharePreparedStatements = sharePreparedStatements;
    }

    public String getFilters() {
        return filters;
    }

    public void setFilters(String filters) {
        this.filters = filters;
    }
}