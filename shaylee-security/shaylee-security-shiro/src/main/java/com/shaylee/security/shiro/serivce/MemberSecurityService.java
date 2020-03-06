package com.shaylee.security.shiro.serivce;

import com.shaylee.security.shiro.entity.MemberSecurity;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.List;

/**
 * 会员登录 服务层
 *
 * @author Beck
 * @date 2019-10-10
 */
public interface MemberSecurityService {

    /**
     * 查询会员登录信息
     *
     * @param id 会员ID
     * @return 会员登录信息
     */
    MemberSecurity getMemberSecurityByMemberId(Long id);

    /**
     * 查询会员登录信息
     *
     * @param memberIds 会员ID
     * @return 会员登录信息
     */
    List<MemberSecurity> getMemberSecurityByMemberIds(Collection<Long> memberIds);

    /**
     * 查询会员登录信息
     *
     * @param memberNo 会员编号
     * @return 会员登录信息
     */
    MemberSecurity getMemberSecurityByMemberNo(String memberNo);

    /**
     * 查询会员登录信息
     *
     * @param areaCode 手机区号
     * @param phone    手机号
     * @return 会员登录信息
     */
    MemberSecurity getMemberSecurityByPhone(String areaCode, String phone);

    /**
     * 获取会员安全信息缓存(不存在会查数据库并刷新缓存)
     *
     * @param memberNo 会员号
     * @return 会员安全信息(memberId, memberNo, password)
     */
    MemberSecurity getMemberSecurityCache(String memberNo);

    /**
     * 修改密码
     *
     * @param areaCode 手机区号
     * @param phone    手机号
     * @param password 密码
     */
    void resetMemberPassword(String areaCode, String phone, String password);

    /**
     * 刷新登陆信息(最后登录IP，最后登陆OS，最后登陆时间)
     *
     * @param request        HttpServletRequest
     * @param memberSecurity 会员安全实体
     */
    void refreshLoginInfo(HttpServletRequest request, MemberSecurity memberSecurity);

    /**
     * 刷新会员安全信息缓存
     *
     * @param memberNo 会员号
     * @return 会员安全信息(memberId, memberNo, password)
     */
    MemberSecurity refreshMemberSecurityCache(String memberNo);

    /**
     * 刷新会员最后活跃时间
     *
     * @param id 数据ID
     */
    void refreshMemberLastLoginDate(Long id);

    /**
     * 登录成功处理
     *
     * @param memberSecurity 会员安全信息
     */
    void loginSuccessHandle(MemberSecurity memberSecurity);

    /**
     * 新增会员登录信息
     *
     * @param memberSecurity 会员登录信息
     * @return 结果
     */
    Integer saveMemberSecurity(MemberSecurity memberSecurity);

    /**
     * 修改会员登录信息
     *
     * @param memberSecurity 会员登录信息
     */
    void updateMemberSecurity(MemberSecurity memberSecurity);

    /**
     * 异步修改会员登录信息
     *
     * @param memberSecurity 会员登录信息
     */
    void updateMemberSecurityAsyn(MemberSecurity memberSecurity);
}
