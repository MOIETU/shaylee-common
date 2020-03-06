package com.shaylee.security.shiro.dao;

import com.shaylee.core.mapper.TkBaseMapper;
import com.shaylee.security.shiro.entity.MemberSecurity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 会员登录 数据层
 * Project: shaylee-common
 *
 * @author Adrian
 * @date 2020-03-06
 */
@Mapper
public interface MemberSecurityDao extends TkBaseMapper<MemberSecurity> {

}