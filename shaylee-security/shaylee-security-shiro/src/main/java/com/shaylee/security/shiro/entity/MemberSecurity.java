package com.shaylee.security.shiro.entity;

import com.shaylee.core.entity.BaseEntity;
import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.Date;

/**
 * 会员登录表 member_security
 * Project: shaylee-common
 *
 * @author Adrian
 * @date 2020-03-06
 */
@Data
@Table(name = "member_security")
public class MemberSecurity extends BaseEntity {
    private static final long serialVersionUID = -7720851132669216094L;

    /**
     * 数据ID
     */
    @Id
    @GeneratedValue(generator = "JDBC")
    private Long id;

    /**
     * 会员ID
     */
    private Long memberId;

    /**
     * 会员号
     */
    private String memberNo;

    /**
     * 密码
     */
    private String password;

    /**
     * 盐值
     */
    private String salt;

    /**
     * 手机区域编号
     */
    private String areaCode;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 账号状态(1发布 2编辑 3冻结 4删除)
     */
    private Integer status;

    /**
     * 异常状态(1正常 2异常)
     */
    private Integer abnormal;

    /**
     * 在线状态(0离线 1在线)
     */
    private Integer online;

    /**
     * 注册IP
     */
    private String registerIp;

    /**
     * 注册时间
     */
    private Date registerDate;

    /**
     * 最后登录IP
     */
    private String lastLoginIp;

    /**
     * 最后登录时间
     */
    private Date lastLoginDate;

    /**
     * 最后登录系统
     */
    private String lastLoginOs;

    /**
     * 最后登录系统类型
     */
    private Integer payDeviceType;

    /**
     * 密码超时日期
     */
    private Date expireDate;

    /**
     * 创建者
     */
    private Long createBy;

    /**
     * 创建日期
     */
    private Date createDate;

    /**
     * 更新者
     */
    private Long updateBy;

    /**
     * 更新日期
     */
    private Date updateDate;

    /**
     * 删除标志(0正常 1删除)
     */
    private Integer delFlag;
}
