package com.eastinno.otransos.core.util;

import javax.servlet.http.HttpServletRequest;

import org.springframework.util.StringUtils;

import com.eastinno.otransos.web.ActionContext;

/**
 * 获取客户端IP
 * 
 * @author maowei
 * @createDate 2014-1-14下午1:16:20
 */
public class ClientIPUtil {
    public static String getIP(HttpServletRequest request) {
        if (request == null) {
            request = ActionContext.getContext().getRequest();
        }
        if (request == null) {
            return null;
        }
        String ip = request.getHeader("x-forwarded-for");
        if (!StringUtils.hasText(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
        }
        if (!StringUtils.hasText(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (!StringUtils.hasText(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
}
