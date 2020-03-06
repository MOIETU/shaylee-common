package com.shaylee.security.shiro.dto;

import com.shaylee.core.dto.BaseDTO;
import lombok.Data;

/**
 * Title: 注册基础信息入参
 * Project: shaylee-common
 *
 * @author Adrian
 * @date 2020-03-06
 */
@Data
public class RegisterRequest extends BaseDTO {
    private static final long serialVersionUID = -6654578907237984923L;
    /**
     * 手机区号
     */
    private String areaCode;
    /**
     * 手机号
     */
    private String phone;
    /**
     * 短信验证码
     */
    private String smsCaptcha;
    /**
     * 密码
     */
    private String password;
    /**
     * 语言(zh-CN zh-HK en-US)
     */
    private String language;
}
