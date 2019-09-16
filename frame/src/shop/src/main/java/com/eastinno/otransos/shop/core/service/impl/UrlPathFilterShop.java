package com.eastinno.otransos.shop.core.service.impl;

import java.io.IOException;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.eastinno.otransos.security.service.ISystemMenuService;
import com.eastinno.otransos.shiro.security.core.UrlPathFilter;
/**
 * 电商权限控制核心
 * @author Administrator
 */
@Component("urlPathFilterShop")
public class UrlPathFilterShop extends UrlPathFilter{
	@Autowired
	private ISystemMenuService systemMenuService;
	@Override
	public boolean isAccessAllowed(ServletRequest request,
			ServletResponse response, Object mappedValue) throws IOException {
		HttpServletRequest req = (HttpServletRequest) request;
		boolean isaccess=super.isAccessAllowed(req, response, mappedValue);
		if(!isaccess){
			setUnauthorizedUrl("/shopManage.java");
			setLoginUrl("/shopManage.java");
		}
		return isaccess;
	}
	
}
