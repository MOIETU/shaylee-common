package com.shaylee.security.shiro.serivce;

import com.shaylee.security.shiro.entity.MemberSecurity;
import com.shaylee.security.shiro.dto.SignInfo;

/**
 * Title: 会员Token 服务
 * Project: shaylee-common
 *
 * @author Adrian
 * @date 2020-03-06
 */
public interface MemberTokenService {

    /**
     * 验证令牌是否有效
     *
     * @param token 令牌
     * @return true有效 false失效
     */
    Boolean isTokenValid(String token);

    /**
     * 刷新会员令牌(生成token且登录信息存Redis)
     *
     * @param memberSecurity 会员登录信息
     * @return JWT签名信息
     */
    SignInfo refreshToken(MemberSecurity memberSecurity);

    /**
     * 保存会员旧token(允许旧token可以继续使用20秒)
     *
     * @param token 旧token
     * @return 失败则表示旧Token已经保存过
     */
    Boolean saveTokenToTemp(String token);

    /**
     * 根据会员号销毁token
     *
     * @param memberNo 会员号
     */
    void destroyToken(String memberNo);

}
