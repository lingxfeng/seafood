package com.eastinno.otransos.security;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.eastinno.otransos.security.domain.Tenant;
import com.eastinno.otransos.security.domain.User;
import com.eastinno.otransos.security.service.impl.TenantServiceImpl;
import com.eastinno.otransos.web.ActionContext;
import com.eastinno.otransos.web.core.FrameworkEngine;

public class TenantContext {
    private static final String DISCO_SECURITY_TENANT = "DISCO_SECURITY_TENANT";

    /**
     * 根据请求URI对应的一级域名获取对应的租户信息
     * 
     * @return
     */
    public static Tenant getTenant() {
        Tenant t = null;
        HttpSession session = ActionContext.getContext().getSession();
        HttpServletRequest req = ActionContext.getContext().getRequest();
        if (session == null || req == null) {
            return null;
        }

        String host = req.getScheme() + "://" + req.getServerName();// 当前域名
        if (req.getServerPort() != 80) {
            host += ":" + req.getServerPort();
        }
        // 1.从当前登陆用户获取租户对象
        User user = UserContext.getUser();
        if(user!=null){
        	t = user.getTenant();
        }
        
        if (t != null && t.getUrl().indexOf(host)!=-1) {
            session.setAttribute(DISCO_SECURITY_TENANT, t);
            return t;
        }
        // 2.从当前SESSION中获取租户对象
        t = (Tenant) session.getAttribute(DISCO_SECURITY_TENANT);
        if (t == null || !host.equals(t.getUrl())) {
            t = getTenantByDB(host);// 3.再根据host从数据库中查找
        }
        session.setAttribute(DISCO_SECURITY_TENANT, t);
        return t;
    }

    /**
     * 根据当前租户域名到数据库中获取对应的租户信息
     * 
     * @param host
     * @return
     */
    private static Tenant getTenantByDB(String host) {
        Tenant t = null;
        TenantServiceImpl tenantService = (TenantServiceImpl) FrameworkEngine.getContainer().getBean("tenantServiceImpl");
        
        t = tenantService.getTenantByHost(host);
        return t;
    }

    public static void main(String[] args) {
        String url = "www.baidu.com,hterp.dgdy.cc,erp.dgdy.cc";
        System.out.println(url.indexOf("erp.dgdy.cc"));
    }
}