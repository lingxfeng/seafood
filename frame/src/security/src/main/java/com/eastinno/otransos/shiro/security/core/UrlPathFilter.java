package com.eastinno.otransos.shiro.security.core;

import java.io.IOException;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authz.PermissionsAuthorizationFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.eastinno.otransos.core.util.CommUtil;
import com.eastinno.otransos.security.domain.SystemLog;
import com.eastinno.otransos.security.domain.Tenant;
import com.eastinno.otransos.security.service.ISystemLogService;

/**
 * 自定义url拦截器
 * 
 * @author nsz
 */
@Component("urlPathFilter")
public class UrlPathFilter extends PermissionsAuthorizationFilter {
	@Autowired
	private ISystemLogService logService;
    public Logger logger = LoggerFactory.getLogger(getClass());
    @Override
    public boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) throws IOException {
        HttpServletRequest req = (HttpServletRequest) request;
        SystemLog log = ShiroUtils.getSystemLog(req);
        if(log==null){
        	return true;
        }else{
        	logService.addSystemLog(log);
        }
        if(!ShiroUtils.hasResource(req,log)){
        	return true;
        }
        Subject subject = getSubject(request, response);
        String actionName = log.getAction();
        String paramscmd = CommUtil.null2String(log.getCmd());
        paramscmd = paramscmd.equals("") ? "INIT" : paramscmd.toUpperCase();
        String permissionstr = actionName + ":" + paramscmd;
        Tenant tenant = ShiroUtils.getTenant(req);
        permissionstr = tenant.getCode()+":"+permissionstr;
        boolean permitted = subject.isPermitted(permissionstr);
        return permitted;
    }
}
