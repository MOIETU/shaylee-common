package com.shaylee.dynamic.datasource.properties;

import lombok.Data;

/**
 * Title: 数据源配置文件映射类
 * Project: shaylee-common
 *
 * @author Adrian
 * @date 2020-02-20
 */
@Data
public class JdbcConnectionProperties {
    private String url;
    private String username;
    private String password;
}
