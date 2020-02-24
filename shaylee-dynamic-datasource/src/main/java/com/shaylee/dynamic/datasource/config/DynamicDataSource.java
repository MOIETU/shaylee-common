package com.shaylee.dynamic.datasource.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.shaylee.dynamic.datasource.utils.DynamicDataSourceContextHolder;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import java.util.Map;

/**
 * Title: 多数据源
 * Project: shaylee-common
 *
 * @author Adrian
 * @version 1.0
 * @date 2020-2-20
 */
public class DynamicDataSource extends AbstractRoutingDataSource {

    /**
     * 重写AbstractRoutingDataSource的determineCurrentLookupKey方法，实现多数据源支持
     */
    @Override
    protected Object determineCurrentLookupKey() {
        return DynamicDataSourceContextHolder.getDataSourceType();
    }

    public DynamicDataSource(DruidDataSource defaultDataSource, Map<Object, Object> targetDataSource) {
        super.setDefaultTargetDataSource(defaultDataSource);
        super.setTargetDataSources(targetDataSource);
    }
}
