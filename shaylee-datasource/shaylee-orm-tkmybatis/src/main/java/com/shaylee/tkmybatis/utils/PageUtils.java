package com.shaylee.tkmybatis.utils;

import com.github.pagehelper.PageInfo;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Title: 分页工具
 * Project: shaylee-common
 *
 * @author Adrian
 * @date 2020-03-02
 */
public class PageUtils {

    /**
     * 分页响应格式
     *
     * @param pageInfo pageHelper分页信息
     * @return 简化分页信息
     */
    public static Map<String, Object> convertPageInfo(PageInfo pageInfo) {
        Map<String, Object> objectMap = new LinkedHashMap<>(5);
        objectMap.put("pageNum", pageInfo.getPageNum());
        objectMap.put("pageSize", pageInfo.getPageSize());
        objectMap.put("totalItem", pageInfo.getTotal());
        objectMap.put("totalPage", pageInfo.getPages());
        objectMap.put("list", pageInfo.getList());
        return objectMap;
    }
}
