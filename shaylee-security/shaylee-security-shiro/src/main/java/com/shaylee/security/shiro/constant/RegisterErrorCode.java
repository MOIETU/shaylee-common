package com.shaylee.security.shiro.constant;

/**
 * Title: 会员注册错误码
 * 错误编码，由5位数字组成，前2位为模块编码，后3位为业务编码
 * 如：11 01 01（11代表移动(10代表通用异常码，12为后台)，01代表注册模块，01代表重设密码用户不存在）
 * Project: shaylee-common
 *
 * @author Adrian
 * @date 2020-03-06
 */
public interface RegisterErrorCode {

    /**
     * 重设密码用户不存在 错误码
     */
    String RESET_PWD_MEMBER_NOT_EXIST = "110101";
}
