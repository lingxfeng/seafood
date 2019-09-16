package com.eastinno.otransos.security.mvc.ajax;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;

import com.eastinno.otransos.core.util.CommUtil;
import com.eastinno.otransos.security.UserContext;
import com.eastinno.otransos.security.annotation.Remark;
import com.eastinno.otransos.web.ActionContext;
import com.eastinno.otransos.web.Module;
import com.eastinno.otransos.web.Page;
import com.eastinno.otransos.web.WebForm;
import com.eastinno.otransos.web.core.AbstractPageCmdAction;

@Remark("登陆模块")
public class LoginAction extends AbstractPageCmdAction {
    /**
     * 登陆
     */
    public Page doInit(WebForm form, Module module) {
        HttpServletRequest request = ActionContext.getContext().getRequest();
        String method = request.getMethod();
        if ("POST".equals(method)) {
            String randomCode = CommUtil.null2String(form.get("randomCode"));
            String randomCodeS = ActionContext.getContext().getSession().getAttribute("rand") + "";
            if (!randomCode.equals(randomCodeS)) {
                form.addResult("erroMsg", "验证码不正确！！！");
            } else {
                String userName = CommUtil.null2String(form.get("username"));
                String password = CommUtil.null2String(form.get("password"));
                UsernamePasswordToken token = new UsernamePasswordToken(userName, password);
                Subject currentUser = SecurityUtils.getSubject();
                try {
                    currentUser.login(token);
                    return new Page("url", "/manage.java", "html");
                } catch (AuthenticationException ae) {
                    form.addResult("erroMsg", "用户名或密码不正确!");
                }
            }
        }
        return new Page("/manage/login.html");
    }

    /**
     * 退出登录
     * 
     * @param form
     * @param module
     * @return
     */
    public Page doLogout(WebForm form, Module module) {
        SecurityUtils.getSubject().logout();
        return new Page("url", "/login.java", "html");
    }

    /**
     * 认证失败
     * 
     * @param form
     * @param module
     * @return
     * @throws IOException
     */
    public Page doUnauth(WebForm form, Module module) throws IOException {
        if (UserContext.getUser() == null) {
            ActionContext.getContext().getResponse().setHeader("LoginRequired", "true");
        }
        ActionContext.getContext().getResponse().setHeader("Unauthorized", "true");
        ActionContext.getContext().getResponse().setHeader("Cache-Control", "no-cache");
        Page p = ActionContext.getContext().getWebInvocationParam().getModule().findPage("PERMISSION_PAGE");// 允许自定义配置权限不足显示页面
        if (p == null)
            p = new Page("PERMISSION_PAGE", "classpath:com/eastinno/otransos/web/exception/permissionError.html");
        return p;
    }
}
