package com.shaylee.security.shiro.constant;

/**
 * Title: 安全相关常量
 * Project: shaylee-common
 *
 * @author Adrian
 * @date 2020-03-06
 */
public class SecurityConstant {

    /**
     * request header定义的token参数名(Authorization或token)
     */
    public static final String REQUEST_HEADER_TOKEN = "token";

    /**
     * APP端自定义realm名
     */
    public static final String REALM_NAME_APP = "app_realm";

    /** JWT Claim名 */
    /**
     * JWT Claim会员编号
     */
    public static final String JWT_CLAIM_MEMBER_NO = "memberNo";
    /**
     * JWT Claim会员ID
     */
    public static final String JWT_CLAIM_MEMBER_ID = "memberId";
    /**
     * JWT短失效时间，5分钟(秒)
     */
    public static final Long JWT_EXPIRE_TIME_SHORT = 5 * 60L;
    /**
     * JWT长失效时间，1个月(秒)
     */
    public static final Long JWT_EXPIRE_TIME = 30 * 24 * 60 * 60L;
    /**
     * JWT刷新后原Token可用时间，20秒(秒)
     */
    public static final Long JWT_EXPIRE_TIME_TEMP = 20L;
    /**
     * JWT会员登录Token存储(结构key->member_token:memberNo value->token)
     */
    public static final String REDIS_KEY_JWT = "member_token:{0}";
    /**
     * 已过期的JWT会员登录Token存储，已过期的Token默认还可以使用20秒(结构key->member_token_temp:memberNo value->token)
     */
    public static final String REDIS_KEY_JWT_TEMP = "member_token_temp:{0}";

    /**
     * 自定义shiro过滤器名
     */
    public static final String SHIRO_FILTER_JWT = "jwt";
    /**
     * shiro未授权URL
     */
    public static final String SHIRO_FILTER_UNAUTHORIZED_URL = "/401/Unauthorized";
}
