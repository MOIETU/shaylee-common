package com.shaylee.security.shiro.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.shaylee.security.shiro.constant.SecurityConstant;
import com.shaylee.security.shiro.entity.MemberSecurity;
import com.shaylee.security.shiro.dto.SignInfo;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * Title: JWT 工具类
 * Project: shaylee-common
 *
 * @author Adrian
 * @date 2020-03-06
 */
public class JwtUtil {

    /**
     * 校验token是否正确
     *
     * @param token          令牌
     * @param memberSecurity 会员安全实体
     * @return 是否正确
     */
    public static boolean verify(String token, MemberSecurity memberSecurity) {
        // 签名基础信息
        Long memberId = memberSecurity.getMemberId();
        String memberNo = memberSecurity.getMemberNo();
        String secret = memberSecurity.getPassword();
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            JWTVerifier verifier = JWT.require(algorithm)
                    .withClaim(SecurityConstant.JWT_CLAIM_MEMBER_ID, memberId)
                    .withClaim(SecurityConstant.JWT_CLAIM_MEMBER_NO, memberNo)
                    .build();
            verifier.verify(token);
            return true;
        } catch (Exception exception) {
            return false;
        }
    }

    /**
     * 获得会员NO
     *
     * @return 会员NO
     */
    public static String getMemberNo(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getClaim(SecurityConstant.JWT_CLAIM_MEMBER_NO).asString();
        } catch (JWTDecodeException e) {
            return null;
        }
    }

    /**
     * 获得会员ID
     *
     * @return 会员ID
     */
    public static Long getMemberId(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getClaim(SecurityConstant.JWT_CLAIM_MEMBER_ID).asLong();
        } catch (JWTDecodeException e) {
            return null;
        }
    }

    /**
     * 获取token失效日期
     *
     * @return 失效日期
     */
    public static Date getExpireDate(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getExpiresAt();
        } catch (JWTDecodeException e) {
            return null;
        }
    }

    /**
     * 生成签名,过期时间SecurityConstant.JWT_EXPIRE_TIME
     *
     * @param memberSecurity 会员安全实体
     * @return 加密的token
     */
    public static SignInfo sign(MemberSecurity memberSecurity) {
        SignInfo signInfo = new SignInfo();
        // 签名基础信息
        Long memberId = memberSecurity.getMemberId();
        String memberNo = memberSecurity.getMemberNo();
        String secret = memberSecurity.getPassword();
        // 配置失效日期
        Date date = new Date(LocalDateTime.now().getSecond() + SecurityConstant.JWT_EXPIRE_TIME);
        // 加密算法配置
        Algorithm algorithm = Algorithm.HMAC256(secret);
        // 附带memberId，memberNo信息
        String token = JWT.create().withClaim(SecurityConstant.JWT_CLAIM_MEMBER_ID, memberId)
                .withClaim(SecurityConstant.JWT_CLAIM_MEMBER_NO, memberNo)
                .withExpiresAt(date).sign(algorithm);
        signInfo.setToken(token);
        signInfo.setExpireDate(date);
        signInfo.setExpire(SecurityConstant.JWT_EXPIRE_TIME);
        return signInfo;
    }
}