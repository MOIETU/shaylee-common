package com.shaylee.core.page;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * Title: 分页结构
 * Project: shaylee-common
 *
 * @author Adrian
 * @date 2020-02-26
 */
@Data
public class PageData<T> implements Serializable {
    private static final long serialVersionUID = 5745665179601318769L;
    /**
     * 当前记录起始索引
     */
    private Integer pageNum;
    /**
     * 每页显示记录数
     */
    private Integer pageSize;
    /**
     * 总页数
     */
    private Long totalPage;
    /**
     * 总记录数
     */
    private Long totalItem;
    /**
     * 结果集
     */
    private List<T> list;
}