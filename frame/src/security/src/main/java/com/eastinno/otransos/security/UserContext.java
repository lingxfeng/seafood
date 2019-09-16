package com.eastinno.otransos.security;

import org.acegisecurity.context.SecurityContextHolder;

import com.eastinno.otransos.security.domain.User;
import com.eastinno.otransos.shiro.security.core.ShiroUtils;
import com.eastinno.otransos.web.ActionContext;

/**
 * 当前用户上下文处理
 * 
 * @Author <a href="mailto:ksmwly@gmail.com">lengyu</a>
 * @Creation date: 2008年9月9日 下午3:03:05
 * @Intro
 */
public class UserContext {
    public static boolean SHIRO = false;// 默认使用shiro权限管理

    public static User getUser() {
        Object user = null;
        try {
            if ((!SHIRO) && (ActionContext.getContext().getSession() != null)) {
                user = ActionContext.getContext().getSession().getAttribute("DISCO_SECURITY_USER");
            } else if (SecurityContextHolder.getContext().getAuthentication() != null) {
                user = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            }
            if ((user != null) && ((user instanceof User))) {
                return (User) user;
            } else {
                return ShiroUtils.getUser();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void setSecurityUser(User user) {
        ActionContext.getContext().getSession().setAttribute("DISCO_SECURITY_USER", user);
    }

    public static User getMember() {
        return (User) ActionContext.getContext().getSession().getAttribute("DISCO_MEMBER");
    }

    public static void logoutMember() {
        ActionContext.getContext().getSession().removeAttribute("DISCO_MEMBER");
    }

    public static void setMember(User user) {
        ActionContext.getContext().getSession().setAttribute("DISCO_MEMBER", user);
    }

    public static void removeUser() {
        User user = getUser();
        if (user != null)
            OnlineUserManage.getInstance().removeUser(user.getName());
        if (SHIRO)
            SecurityContextHolder.clearContext();
        else
            ActionContext.getContext().getSession().removeAttribute("DISCO_SECURITY_USER");
    }

}