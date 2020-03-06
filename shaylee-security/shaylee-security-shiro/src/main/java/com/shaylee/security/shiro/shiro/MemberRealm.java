package com.shaylee.security.shiro.shiro;

import com.shaylee.redis.service.CacheService;
import com.shaylee.security.shiro.constant.SecurityConstant;
import com.shaylee.security.shiro.entity.MemberSecurity;
import com.shaylee.security.shiro.serivce.MemberSecurityService;
import com.shaylee.security.shiro.utils.JwtUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;

/**
 * Title: App端认证授权逻辑
 * Project: shaylee-common
 *
 * @author Adrian
 * @date 2020-03-06
 */
@Service
public class MemberRealm extends AuthorizingRealm {

    private static final Logger logger = LoggerFactory.getLogger(MemberRealm.class);

    @Autowired
    private MemberSecurityService memberSecurityService;
    @Autowired
    private CacheService redisCache;

    /**
     * 授权
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        return null;
    }

    /**
     * 认证
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken auth) throws AuthenticationException {
        String token = (String) auth.getCredentials();
        // 解密签名的token获得memberNo
        Long memberId = JwtUtil.getMemberId(token);
        String memberNo = JwtUtil.getMemberNo(token);
        if (memberNo == null) {
            logger.debug("token invalid");
            throw new AuthenticationException("token invalid");
        }
        // 校验token是否失效
        String tokenKey = MessageFormat.format(SecurityConstant.REDIS_KEY_JWT, memberNo);
        Object tokenCache = redisCache.get(tokenKey);
        if (!token.equals(tokenCache)) {
            // 检查临时token是否匹配
            String tempTokenKey = MessageFormat.format(SecurityConstant.REDIS_KEY_JWT_TEMP, memberNo);
            Object tempTokenCache = redisCache.get(tempTokenKey);
            if (!token.equals(tempTokenCache)) {
                // token与临时token都失效，验证失败
                logger.debug("token invalid");
                throw new AuthenticationException("token invalid");
            }
        }
        // 检查会员是否存在
        MemberSecurity memberSecurity = memberSecurityService.getMemberSecurityCache(memberNo);
        if (memberSecurity == null) {
            logger.debug("Member didn't existed! token:" + token);
            throw new AuthenticationException("Member didn't existed!");
        }
        // 检验会员token是否匹配登录信息
        memberSecurity.setMemberId(memberId);
        memberSecurity.setMemberNo(memberNo);
        if (!JwtUtil.verify(token, memberSecurity)) {
            logger.debug("token verify error! token:" + token);
            throw new AuthenticationException("token verify error");
        }
        return new SimpleAuthenticationInfo(token, token, SecurityConstant.REALM_NAME_APP);
    }

    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JwtToken;
    }

    public void clearCachedAuthorizationInfo() {
        this.clearCachedAuthorizationInfo(SecurityUtils.getSubject().getPrincipals());
    }
}
