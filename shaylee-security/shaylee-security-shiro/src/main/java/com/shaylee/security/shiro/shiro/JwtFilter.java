package com.shaylee.security.shiro.shiro;

import com.shaylee.security.shiro.constant.SecurityConstant;
import com.shaylee.security.shiro.entity.MemberSecurity;
import com.shaylee.security.shiro.serivce.MemberSecurityService;
import com.shaylee.security.shiro.serivce.MemberTokenService;
import com.shaylee.security.shiro.utils.JwtUtil;
import com.shaylee.security.shiro.dto.SignInfo;
import org.apache.shiro.web.filter.authc.BasicHttpAuthenticationFilter;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * Title: jwt过滤器
 * Project: shaylee-common
 *
 * @author Adrian
 * @date 2020-03-06
 */
public class JwtFilter extends BasicHttpAuthenticationFilter {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * response header属性名：指定允许其他域名访问
     */
    private static final String ACCESS_CONTROL_ALLOW_ORIGIN = "Access-Control-Allow-Origin";
    /**
     * response header属性名：允许的请求类型
     */
    private static final String ACCESS_CONTROL_ALLOW_METHODS = "Access-Control-Allow-Methods";
    /**
     * response header属性名：允许的请求头字段
     */
    private static final String ACCESS_CONTROL_ALLOW_HEADERS = "Access-Control-Allow-Headers";

    /**
     * request header属性名：域名
     */
    private static final String REQUEST_HEADER_PROPERTY_ORIGIN = "Origin";
    /**
     * request header属性名：请求类型
     */
    private static final String REQUEST_HEADER_PROPERTY_ALLOW_METHODS = "GET,POST,OPTIONS,PUT,DELETE";
    /**
     * request header属性名：请求头字段
     */
    private static final String REQUEST_HEADER_PROPERTY_HEADERS = "Access-Control-Request-Headers";
    /**
     * response header跨域响应头
     */
    private static final String RESPONSE_HEADER_PROPERTY_HEADERS = "Access-Control-Expose-Headers";

    /**
     * response字符编码
     */
    private static final String RESPONSE_PROPERTY_CHARACTER_ENCODING = "UTF-8";
    /**
     * response响应格式
     */
    private static final String RESPONSE_PROPERTY_CONTENT_TYPE = "application/json;charset=UTF-8";

    private static final String MEMBER_TOKEN_SERVICE_BEAN_NAME = "memberTokenService";

    private static final String MEMBER_SECURITY_SERVICE_BEAN_NAME = "memberSecurityService";

    private MemberTokenService memberTokenService;

    /**
     * 判断用户是否想要登入。
     * 检测header里面是否包含token字段(如果包含token字段，需要解析token并且判断token是否失效，失效则以游客方式请求接口)
     */
    @Override
    protected boolean isLoginAttempt(ServletRequest request, ServletResponse response) {
        HttpServletRequest req = (HttpServletRequest) request;
        String authorization = req.getHeader(SecurityConstant.REQUEST_HEADER_TOKEN);
        BeanFactory factory = WebApplicationContextUtils.getRequiredWebApplicationContext(request.getServletContext());
        memberTokenService = (MemberTokenService) factory.getBean(MEMBER_TOKEN_SERVICE_BEAN_NAME);
        return memberTokenService.isTokenValid(authorization);
    }

    /**
     * 执行登录逻辑
     */
    @Override
    protected boolean executeLogin(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        String authorization = httpServletRequest.getHeader(SecurityConstant.REQUEST_HEADER_TOKEN);
        JwtToken token = new JwtToken(authorization);
        // 提交给realm进行登入，如果错误他会抛出异常并被捕获
        getSubject(request, response).login(token);
        // 如果没有抛出异常则代表登入成功，返回true
        return true;
    }

    /**
     * 所有接口都允许访问(用于区分会员和非会员)
     */
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        if (isLoginAttempt(request, response)) {
            try {
                executeLogin(request, response);
                // 执行完realm进行登入没有异常，超过短失效时长则刷新token
                this.refreshToken(request, response);
            } catch (Exception e) {
                /*response401(response, e.getMessage());*/
            }
        }
        return true;
    }

    /**
     * 对跨域提供支持
     */
    @Override
    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        httpServletResponse.setHeader(ACCESS_CONTROL_ALLOW_ORIGIN, httpServletRequest.getHeader(REQUEST_HEADER_PROPERTY_ORIGIN));
        httpServletResponse.setHeader(ACCESS_CONTROL_ALLOW_METHODS, REQUEST_HEADER_PROPERTY_ALLOW_METHODS);
        httpServletResponse.setHeader(ACCESS_CONTROL_ALLOW_HEADERS, httpServletRequest.getHeader(REQUEST_HEADER_PROPERTY_HEADERS));
        httpServletResponse.setCharacterEncoding(RESPONSE_PROPERTY_CHARACTER_ENCODING);
        httpServletResponse.setContentType(RESPONSE_PROPERTY_CONTENT_TYPE);
        // 跨域时会首先发送一个option请求，这里我们给option请求直接返回正常状态
        if (httpServletRequest.getMethod().equals(RequestMethod.OPTIONS.name())) {
            httpServletResponse.setStatus(HttpStatus.OK.value());
            return false;
        }
        return super.preHandle(request, response);
    }

    protected void responseError(ServletRequest servletRequest, ServletResponse servletResponse) {
        HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;
        httpResponse.setCharacterEncoding(RESPONSE_PROPERTY_CHARACTER_ENCODING);
        httpResponse.setContentType(RESPONSE_PROPERTY_CONTENT_TYPE);
        httpResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
    }

    /**
     * 将非法请求跳转到 /401
     */
    private void response401(ServletResponse resp, String message) {
        try {
            HttpServletResponse httpServletResponse = (HttpServletResponse) resp;
            httpServletResponse.sendRedirect("/401/" + message);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    /**
     * 超过5分钟刷新token
     */
    private void refreshToken(ServletRequest request, ServletResponse response) {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        String token = httpServletRequest.getHeader(SecurityConstant.REQUEST_HEADER_TOKEN);
        // 解析token
        String memberNo = JwtUtil.getMemberNo(token);
        Date expireDate = JwtUtil.getExpireDate(token);
        // 获取Security实体
        BeanFactory factory = WebApplicationContextUtils.getRequiredWebApplicationContext(request.getServletContext());
        MemberSecurityService memberSecurityService = (MemberSecurityService) factory.getBean(MEMBER_SECURITY_SERVICE_BEAN_NAME);
        MemberSecurity memberSecurity = memberSecurityService.getMemberSecurityCache(memberNo);
        if (expireDate == null) {
            return;
        }
        // 计算token失效时间
        if (LocalDateTime.now().getSecond() - SecurityConstant.JWT_EXPIRE_TIME_SHORT > expireDate.getTime() - SecurityConstant.JWT_EXPIRE_TIME) {
            // 当前时间 - 旧token生成时间 > 5分钟。将旧token存到临时token
            if (memberTokenService.saveTokenToTemp(token)) {
                // 生成新token
                SignInfo signInfo = memberTokenService.refreshToken(memberSecurity);
                // 异步更新会员最后活跃时间
                memberSecurityService.refreshMemberLastLoginDate(memberSecurity.getId());
                // 最后将刷新的Token存放在Response的Header中的token字段返回
                HttpServletResponse httpServletResponse = WebUtils.toHttp(response);
                httpServletResponse.setHeader(SecurityConstant.REQUEST_HEADER_TOKEN, signInfo.getToken());
                httpServletResponse.setHeader(RESPONSE_HEADER_PROPERTY_HEADERS, SecurityConstant.REQUEST_HEADER_TOKEN);
            }
        }
    }

}