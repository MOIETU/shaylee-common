package com.shaylee.security.shiro.dto;

import com.shaylee.core.dto.BaseDTO;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;
import java.util.List;

/**
 * Title: 注册详细信息入参
 * Project: shaylee-common
 *
 * @author Adrian
 * @date 2020-03-06
 */
@Data
public class RegisterExtendRequest extends BaseDTO {
    private static final long serialVersionUID = 8225781888787899547L;
    /**
     * 会员ID
     */
    private Long memberId;
    /**
     * 昵称
     */
    private String name;
    /**
     * 性别(1男 2女)
     */
    private Integer gender;
    /**
     * 头像资源ID(membere_attach)
     */
    private Long iconId;
    /**
     * 职业
     */
    private String career;
    /**
     * 出生日期
     */
    private Date birthday;
    /**
     * 身高
     */
    private Integer height;
    /**
     * 体重
     */
    private Integer weight;
    /**
     * APP版本类型(1安卓 2苹果)
     */
    private Integer deviceType;
    /**
     * 当前完善信息的ip
     */
    private String ip;
}
