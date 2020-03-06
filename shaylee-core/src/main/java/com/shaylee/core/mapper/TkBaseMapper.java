package com.shaylee.core.mapper;

import tk.mybatis.mapper.common.ConditionMapper;
import tk.mybatis.mapper.common.IdsMapper;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.special.InsertListMapper;

/**
 * Title: 基础查询接口(Mapper继承该接口后，无需编写mapper.xml文件，即可获得CRUD功能)
 * Project: shaylee-common
 *
 * @author Adrian
 * @date 2020-02-26
 */
public interface TkBaseMapper<T> extends Mapper<T>, IdsMapper<T>, InsertListMapper<T>, ConditionMapper<T> {
}
