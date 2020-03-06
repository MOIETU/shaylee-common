package com.shaylee.security.shiro.utils;

import com.shaylee.security.shiro.dto.MemberLoginDTO;
import com.shaylee.security.shiro.entity.MemberSecurity;
import com.shaylee.security.shiro.password.BCryptPasswordEncoder;
import com.shaylee.security.shiro.password.PasswordEncoder;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;

/**
 * Title: 密码加密 工具类
 * Project: shaylee-common
 *
 * @author Adrian
 * @date 2020-03-06
 */
public class PasswordUtils {
    private static PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    /**
     * 加密
     *
     * @param memberLogin 会员登录实体(会员号+明文密码+盐)
     * @return 返回加密字符串 BCrypt(会员号+明文密码+盐)
     */
    public static String encode(MemberLoginDTO memberLogin) {
        String memberNo = memberLogin.getMemberNo();
        String password = memberLogin.getPassword();
        String salt = memberLogin.getSalt();
        String plainPassword = memberNo + password + salt;
        String cryptograph = passwordEncoder.encode(plainPassword);
        memberLogin.setCryptograph(cryptograph);
        return cryptograph;
    }

    /**
     * 比较密码是否相等
     *
     * @param password       用户输入密码
     * @param memberSecurity 会员登录实体(memberNo,salt,password)
     * @return true：成功 false：失败
     */
    public static boolean matches(String password, MemberSecurity memberSecurity) {
        String memberNo = memberSecurity.getMemberNo();
        String salt = memberSecurity.getSalt();
        String plainPassword = memberNo + password + salt;
        String encryptPassword = memberSecurity.getPassword();
        return passwordEncoder.matches(plainPassword, encryptPassword);
    }

    /**
     * 生成随机盐
     */
    public static String randomSalt() {
        // 一个Byte占两个字节，此处生成的16字节，字符串长度为32
        SecureRandomNumberGenerator secureRandom = new SecureRandomNumberGenerator();
        return secureRandom.nextBytes(16).toHex();
    }
}
