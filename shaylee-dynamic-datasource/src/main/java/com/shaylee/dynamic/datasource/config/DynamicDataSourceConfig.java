package com.shaylee.dynamic.datasource.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.shaylee.dynamic.datasource.properties.DataSourceProperties;
import com.shaylee.dynamic.datasource.properties.JdbcConnectionProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Title: 配置多数据源
 * Project: shaylee-common
 *
 * @author Adrian
 * @date 2020-02-20
 */
@Configuration
public class DynamicDataSourceConfig {

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.druid")
    public DataSourceProperties dataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean
    public DynamicDataSource dynamicDataSource(DataSourceProperties dataSourceProperties) {
        Map<String, DruidDataSource> druidDataSourceMap = this.buildDruidDataSourceMap(dataSourceProperties);
        // 默认数据源
        DruidDataSource defaultDataSource = druidDataSourceMap.get(dataSourceProperties.getDefaultDatasource());
        // 目标数据源
        Map<Object, Object> targetDataSource = this.getDynamicDataSource(druidDataSourceMap);
        return new DynamicDataSource(defaultDataSource, targetDataSource);
    }

    private Map<Object, Object> getDynamicDataSource(Map<String, DruidDataSource> druidDataSourceMap) {
        Map<Object, Object> targetDataSources = new HashMap<>(druidDataSourceMap.size());
        druidDataSourceMap.forEach(targetDataSources::put);
        return targetDataSources;
    }

    private Map<String, DruidDataSource> buildDruidDataSourceMap(DataSourceProperties dataSourceProperties) {
        Map<String, JdbcConnectionProperties> jdbcConnectionPropertiesMap = new LinkedHashMap<>();
        Map<String, JdbcConnectionProperties> masterConnectionPropertiesMap = dataSourceProperties.getMaster();
        Map<String, JdbcConnectionProperties> slaveConnectionPropertiesMap = dataSourceProperties.getSlave();
        jdbcConnectionPropertiesMap.putAll(masterConnectionPropertiesMap);
        jdbcConnectionPropertiesMap.putAll(slaveConnectionPropertiesMap);
        Map<String, DruidDataSource> targetDataSources = new HashMap<>(jdbcConnectionPropertiesMap.size());
        jdbcConnectionPropertiesMap.forEach((dataSourceType, jdbcConnectionProperties) -> {
            DruidDataSource druidDataSource = this.buildDruidDataSource(dataSourceProperties);
            druidDataSource.setUrl(jdbcConnectionProperties.getUrl());
            druidDataSource.setUsername(jdbcConnectionProperties.getUsername());
            druidDataSource.setPassword(jdbcConnectionProperties.getPassword());
            // 初始化数据源
            try {
                druidDataSource.setFilters(dataSourceProperties.getFilters());
                druidDataSource.init();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            targetDataSources.put(dataSourceType, druidDataSource);
        });
        return targetDataSources;
    }

    private DruidDataSource buildDruidDataSource(DataSourceProperties properties) {
        DruidDataSource druidDataSource = new DruidDataSource();
        druidDataSource.setDriverClassName(properties.getDriverClassName());
        druidDataSource.setInitialSize(properties.getInitialSize());
        druidDataSource.setMaxActive(properties.getMaxActive());
        druidDataSource.setMinIdle(properties.getMinIdle());
        druidDataSource.setMaxWait(properties.getMaxWait());
        druidDataSource.setTimeBetweenEvictionRunsMillis(properties.getTimeBetweenEvictionRunsMillis());
        druidDataSource.setMinEvictableIdleTimeMillis(properties.getMinEvictableIdleTimeMillis());
        druidDataSource.setMaxEvictableIdleTimeMillis(properties.getMaxEvictableIdleTimeMillis());
        druidDataSource.setValidationQuery(properties.getValidationQuery());
        druidDataSource.setValidationQueryTimeout(properties.getValidationQueryTimeout());
        druidDataSource.setTestOnBorrow(properties.isTestOnBorrow());
        druidDataSource.setTestOnReturn(properties.isTestOnReturn());
        druidDataSource.setPoolPreparedStatements(properties.isPoolPreparedStatements());
        druidDataSource.setMaxOpenPreparedStatements(properties.getMaxOpenPreparedStatements());
        druidDataSource.setSharePreparedStatements(properties.isSharePreparedStatements());
        return druidDataSource;
    }
}