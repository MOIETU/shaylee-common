package com.shaylee.security.shiro.constant;

import com.shaylee.core.constant.BaseConstant;

/**
 * Title: 会员登录注册 常量类
 * Project: shaylee-common
 *
 * @author Adrian
 * @date 2020-03-06
 */
public class MemberSecurityConstant implements BaseConstant {

    /**
     * 会员安全实体key(缓存结构 member_security field:{memberNo} value:{memberSecurity})
     */
    public static final String REDIS_KEY_MEMBER_SECURITY = "member_security";

}
