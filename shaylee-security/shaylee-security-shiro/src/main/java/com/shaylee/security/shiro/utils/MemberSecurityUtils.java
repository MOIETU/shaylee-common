package com.shaylee.security.shiro.utils;

import org.apache.shiro.SecurityUtils;

import java.util.Date;

/**
 * Title: APP端安全工具
 * Project: shaylee-common
 *
 * @author Adrian
 * @date 2020-03-06
 */
public class MemberSecurityUtils extends SecurityUtils {

    /**
     * 获取当前会员ID
     *
     * @return 会员ID
     */
    public static Long getCurrentMemberId() {
        String token = (String) SecurityUtils.getSubject().getPrincipal();
        return JwtUtil.getMemberId(token);
    }

    /**
     * 获取当前会员编号
     *
     * @return 会员编号
     */
    public static String getCurrentMemberNo() {
        String token = (String) SecurityUtils.getSubject().getPrincipal();
        return JwtUtil.getMemberNo(token);
    }

    /**
     * 获取Token失效日期
     *
     * @return 失效日期
     */
    public static Date getTokenExpireDate() {
        String token = (String) SecurityUtils.getSubject().getPrincipal();
        return JwtUtil.getExpireDate(token);
    }

    /**
     * 获取Token
     *
     * @return Token
     */
    public static String getToken() {
        return (String) SecurityUtils.getSubject().getPrincipal();
    }
}
