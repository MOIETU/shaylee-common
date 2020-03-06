package com.shaylee.security.shiro.dto;

import com.shaylee.core.dto.BaseDTO;
import com.shaylee.security.shiro.entity.MemberSecurity;
import lombok.Data;

/**
 * Title: 会员登录成功信息
 * Project: shaylee-common
 *
 * @author Adrian
 * @date 2020-03-06
 */
@Data
public class LoginSuccessDTO extends BaseDTO {
    private static final long serialVersionUID = -5811626688214784322L;
    /**
     * 会员安全信息
     */
    private MemberSecurity memberSecurity;
}
