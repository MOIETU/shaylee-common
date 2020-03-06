package com.shaylee.security.shiro.serivce.impl;

import com.google.common.base.Joiner;
import com.shaylee.amqp.service.AmqpService;
import com.shaylee.core.exception.BaseException;
import com.shaylee.core.utils.UserAgentUtils;
import com.shaylee.redis.service.CacheService;
import com.shaylee.security.shiro.constant.MemberSecurityConstant;
import com.shaylee.security.shiro.constant.RegisterErrorCode;
import com.shaylee.security.shiro.dao.MemberSecurityDao;
import com.shaylee.security.shiro.dto.LoginSuccessDTO;
import com.shaylee.security.shiro.dto.MemberLoginDTO;
import com.shaylee.security.shiro.entity.MemberSecurity;
import com.shaylee.security.shiro.serivce.MemberSecurityService;
import com.shaylee.security.shiro.utils.PasswordUtils;
import eu.bitwalker.useragentutils.UserAgent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * 会员登录 服务层实现
 * Project: shaylee-common
 *
 * @author Adrian
 * @date 2020-03-06
 */
@Service("memberSecurityService")
public class MemberSecurityServiceImpl implements MemberSecurityService {

    @Autowired
    private MemberSecurityDao memberSecurityDao;
    @Autowired
    private CacheService redisCache;
    @Autowired
    private AmqpService messageQueue;

    private static final String MEMBER_SECURITY_EXCHANGE = "member_security_exchange";
    private static final String MEMBER_SECURITY_ROUTHINGKEY = "member_security_key";

    public final static String MEMBER_LOGIN_EXCHANGE = "member_login_exchange";
    public final static String MEMBER_LOGIN_ROUTHINGKEY = "member_login_key";

    @Override
    public MemberSecurity getMemberSecurityByMemberId(Long memberId) {
        MemberSecurity memberSecurity = new MemberSecurity();
        memberSecurity.setMemberId(memberId);
        return memberSecurityDao.selectOne(memberSecurity);
    }

    @Override
    public List<MemberSecurity> getMemberSecurityByMemberIds(Collection<Long> memberIds) {
        String ids = Joiner.on(",").join(memberIds);
        return memberSecurityDao.selectByIds(ids);
    }

    @Override
    public MemberSecurity getMemberSecurityByMemberNo(String memberNo) {
        MemberSecurity memberSecurity = new MemberSecurity();
        memberSecurity.setMemberNo(memberNo);
        return memberSecurityDao.selectOne(memberSecurity);
    }

    @Override
    public MemberSecurity getMemberSecurityByPhone(String areaCode, String phone) {
        MemberSecurity memberSecurity = new MemberSecurity();
        memberSecurity.setAreaCode(areaCode);
        memberSecurity.setPhone(phone);
        return memberSecurityDao.selectOne(memberSecurity);
    }

    @Override
    public void refreshLoginInfo(HttpServletRequest request, MemberSecurity memberSecurity) {
        UserAgent userAgent = UserAgentUtils.parseUserAgent(request);
        String ip = UserAgentUtils.getIpAddr(request);
        String os = userAgent.getOperatingSystem().getName();
        Date currentDate = new Date();
        MemberSecurity memberSecurityParam = new MemberSecurity();
        memberSecurityParam.setLastLoginIp(ip);
        memberSecurityParam.setLastLoginOs(os);
        memberSecurityParam.setLastLoginDate(currentDate);
        memberSecurityParam.setUpdateDate(currentDate);
        memberSecurityParam.setPayDeviceType(memberSecurity.getPayDeviceType());
        memberSecurityParam.setId(memberSecurity.getId());
        memberSecurityDao.updateByPrimaryKeySelective(memberSecurityParam);
    }

    @Override
    public MemberSecurity refreshMemberSecurityCache(String memberNo) {
        MemberSecurity memberSecurity = this.getMemberSecurityByMemberNo(memberNo);
        redisCache.hSet(MemberSecurityConstant.REDIS_KEY_MEMBER_SECURITY, memberNo, memberSecurity);
        return memberSecurity;
    }

    @Override
    public void refreshMemberLastLoginDate(Long id) {
        MemberSecurity memberSecurity = new MemberSecurity();
        memberSecurity.setId(id);
        memberSecurity.setLastLoginDate(new Date());
        this.updateMemberSecurityAsyn(memberSecurity);
    }

    @Override
    public MemberSecurity getMemberSecurityCache(String memberNo) {
        MemberSecurity memberSecurity;
        Object cacheDTO = redisCache.hGet(MemberSecurityConstant.REDIS_KEY_MEMBER_SECURITY, memberNo);
        if (cacheDTO != null) {
            memberSecurity = (MemberSecurity) cacheDTO;
        } else {
            // 缓存找不到，刷新缓存
            memberSecurity = this.refreshMemberSecurityCache(memberNo);
        }
        return memberSecurity;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void resetMemberPassword(String areaCode, String phone, String password) {
        MemberSecurity memberSecurity = this.getMemberSecurityByPhone(areaCode, phone);
        if (memberSecurity == null) {
            throw new BaseException(RegisterErrorCode.RESET_PWD_MEMBER_NOT_EXIST);
        }
        Date currentDate = new Date();
        String salt = PasswordUtils.randomSalt();
        String memberNo = memberSecurity.getMemberNo();
        // 密码加密
        MemberLoginDTO memberLoginVO = new MemberLoginDTO(memberNo, password, salt);
        String cryptograph = PasswordUtils.encode(memberLoginVO);
        // 设置Security属性
        memberSecurity.setSalt(salt);
        memberSecurity.setPassword(cryptograph);
        memberSecurity.setUpdateDate(currentDate);
        this.updateMemberSecurity(memberSecurity);
        // 删除缓存(用户更新密码成功，再将旧密码的缓存删除)
        redisCache.hDel(MemberSecurityConstant.REDIS_KEY_MEMBER_SECURITY, memberNo);
    }

    @Override
    public void loginSuccessHandle(MemberSecurity memberSecurity) {
        LoginSuccessDTO loginSuccessVO = new LoginSuccessDTO();
        loginSuccessVO.setMemberSecurity(memberSecurity);
        messageQueue.sendMessage(MEMBER_LOGIN_EXCHANGE, MEMBER_LOGIN_ROUTHINGKEY, loginSuccessVO);
    }

    @Override
    public Integer saveMemberSecurity(MemberSecurity memberSecurity) {
        return memberSecurityDao.insertSelective(memberSecurity);
    }

    @Override
    public void updateMemberSecurity(MemberSecurity memberSecurity) {
        memberSecurityDao.updateByPrimaryKeySelective(memberSecurity);
    }

    @Override
    public void updateMemberSecurityAsyn(MemberSecurity memberSecurity) {
        messageQueue.sendMessage(MEMBER_SECURITY_EXCHANGE, MEMBER_SECURITY_ROUTHINGKEY, memberSecurity);
    }
}
