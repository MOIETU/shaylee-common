package com.shaylee.security.shiro.serivce.impl;

import com.shaylee.redis.service.CacheService;
import com.shaylee.security.shiro.constant.SecurityConstant;
import com.shaylee.security.shiro.entity.MemberSecurity;
import com.shaylee.security.shiro.serivce.MemberTokenService;
import com.shaylee.security.shiro.utils.JwtUtil;
import com.shaylee.security.shiro.dto.SignInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.time.LocalDateTime;

/**
 * Title: 会员Token 服务实现
 * Project: shaylee-common
 *
 * @author Adrian
 * @date 2020-03-06
 */
@Service("memberTokenService")
public class MemberTokenServiceImpl implements MemberTokenService {

    @Autowired
    private CacheService redisCache;

    @Override
    public Boolean isTokenValid(String token) {
        // token为空
        if (StringUtils.isBlank(token)) {
            return false;
        }
        // token解析memberNo为空
        String memberNo = JwtUtil.getMemberNo(token);
        if (memberNo == null) {
            return false;
        }
        // token是否有效
        String memberTokenKey = MessageFormat.format(SecurityConstant.REDIS_KEY_JWT, memberNo);
        Object tokenCache = redisCache.get(memberTokenKey);
        if (!token.equals(tokenCache)) {
            // 临时token是否有效
            String tempTokenKey = MessageFormat.format(SecurityConstant.REDIS_KEY_JWT_TEMP, memberNo);
            Object tempTokenCache = redisCache.get(tempTokenKey);
            return token.equals(tempTokenCache);
        }
        return true;
    }

    @Override
    public SignInfo refreshToken(MemberSecurity memberSecurity) {
        SignInfo signInfo = JwtUtil.sign(memberSecurity);
        // token信息写入缓存(毫秒失效)
        String memberTokenKey = MessageFormat.format(SecurityConstant.REDIS_KEY_JWT, memberSecurity.getMemberNo());
        // 删除旧token
        redisCache.delete(memberTokenKey);
        // 添加新token
        redisCache.set(memberTokenKey, signInfo.getToken(), (signInfo.getExpireDate().getTime() - LocalDateTime.now().getSecond()));
        return signInfo;
    }

    @Override
    public Boolean saveTokenToTemp(String token) {
        String memberNo = JwtUtil.getMemberNo(token);
        if (memberNo == null) {
            return false;
        }
        String memberTokenKey = MessageFormat.format(SecurityConstant.REDIS_KEY_JWT_TEMP, memberNo);
        if (redisCache.get(memberTokenKey) != null) {
            return false;
        }
        // token信息写入缓存,设置失效时间20秒
        redisCache.set(memberTokenKey, token, SecurityConstant.JWT_EXPIRE_TIME_TEMP);
        return true;
    }

    @Override
    public void destroyToken(String memberNo) {
        // token信息写入缓存(毫秒失效)
        String memberTokenKey = MessageFormat.format(SecurityConstant.REDIS_KEY_JWT, memberNo);
        redisCache.delete(memberTokenKey);
    }
}
