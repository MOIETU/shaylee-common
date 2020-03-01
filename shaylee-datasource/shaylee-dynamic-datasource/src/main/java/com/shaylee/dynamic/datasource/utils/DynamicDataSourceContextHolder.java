package com.shaylee.dynamic.datasource.utils;

import com.shaylee.dynamic.datasource.enums.DataSourceType;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * Title: 多数据源上下文
 * Project: shaylee-common
 *
 * @author Adrian
 * @date 2020-02-20
 */
public class DynamicDataSourceContextHolder {
    /**
     * 线程自动恢复到上一个数据源
     */
    private static final ThreadLocal<Deque<String>> CONTEXT_HOLDER = ThreadLocal.withInitial(ArrayDeque::new);

    /**
     * 设置当前线程数据源
     *
     * @param dataSourceType 数据源名称
     */
    public static void setDataSourceType(DataSourceType dataSourceType) {
        CONTEXT_HOLDER.get().push(dataSourceType.getName());
    }

    /**
     * 获得当前线程数据源
     *
     * @return 数据源名称
     */
    public static String getDataSourceType() {
        return CONTEXT_HOLDER.get().peek();
    }

    /**
     * 清空当前线程数据源
     */
    public static void clearDataSourceType() {
        Deque<String> deque = CONTEXT_HOLDER.get();
        deque.poll();
        if (deque.isEmpty()) {
            CONTEXT_HOLDER.remove();
        }
    }
}
