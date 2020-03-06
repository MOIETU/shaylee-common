package com.shaylee.security.shiro.dto;

import com.shaylee.core.dto.BaseDTO;
import lombok.Data;

import java.util.Date;

/**
 * Title: JWT签名信息
 * Project: shaylee-common
 *
 * @author Adrian
 * @date 2020-03-06
 */
@Data
public class SignInfo extends BaseDTO {
    private static final long serialVersionUID = -4353214227007954365L;
    /**
     * 令牌
     */
    private String token;
    /**
     * 失效日期
     */
    private Date expireDate;
    /**
     * 失效时长(毫秒)
     */
    private Long expire;
}
