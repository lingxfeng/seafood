package com.eastinno.otransos.shiro.security.core;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.stereotype.Component;

/**
 * 登录验证拦截器
 * 
 * @author nsz
 */
@Component("authcFilter")
public class AuthcFilter extends FormAuthenticationFilter {
    /**
     * 登录重定向拦截器，shiro默认是跳转到上个页面，修改为配置文件中配置的路径
     */
    @Override
    protected void issueSuccessRedirect(ServletRequest request, ServletResponse response) throws Exception {
        WebUtils.issueRedirect(request, response, getSuccessUrl());
    }

    /**
     * 同一客户端重复登陆过滤
     */
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        try {
            // 先判断是否是登录操作
            if (isLoginSubmission(request, response)) {
                return false;
            }
        } catch (Exception e) {
        }
        return super.isAccessAllowed(request, response, mappedValue);

    }

}
