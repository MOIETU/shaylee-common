package com.shaylee.security.shiro.dto;

import lombok.Data;

/**
 * Title: 会员登录信息
 * Project: shaylee-common
 *
 * @author Adrian
 * @date 2020-03-06
 */
@Data
public class MemberLoginDTO {

    /**
     * 会员号
     */
    private String memberNo;

    /**
     * 密码原文
     */
    private String password;

    /**
     * 盐
     */
    private String salt;

    /**
     * 密码密文
     */
    private String cryptograph;

    public MemberLoginDTO() {
    }

    public MemberLoginDTO(String memberNo, String password, String salt) {
        this.memberNo = memberNo;
        this.password = password;
        this.salt = salt;
    }
}
