package com.eastinno.otransos.security.mvc.ajax;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

import com.eastinno.otransos.container.annonation.Action;
import com.eastinno.otransos.container.annonation.Inject;
import com.eastinno.otransos.container.annonation.InjectDisable;
import com.eastinno.otransos.core.support.query.QueryObject;
import com.eastinno.otransos.core.util.CommUtil;
import com.eastinno.otransos.security.UserContext;
import com.eastinno.otransos.security.domain.Tenant;
import com.eastinno.otransos.security.domain.User;
import com.eastinno.otransos.security.service.ITenantService;
import com.eastinno.otransos.security.service.IUserService;
import com.eastinno.otransos.shiro.security.core.ShiroUtils;
import com.eastinno.otransos.web.Module;
import com.eastinno.otransos.web.Page;
import com.eastinno.otransos.web.WebForm;
import com.eastinno.otransos.web.core.AbstractPageCmdAction;

/**
 * 公共控制
 * 
 * @author Administrator
 */
@Action
public class CommonAction extends AbstractPageCmdAction {
    @InjectDisable
    private static final Logger logger = Logger.getLogger(CommonAction.class);
    @Inject
    private IUserService userService;
    @Inject
    private ITenantService tenantService;

    public Page doHomePage(WebForm form, Module module) {
        return new Page("/common/homePage.html");

    }

    public Page doGetCurrentUser(WebForm form) {
        User user = UserContext.getUser();
        if (user == null) {
            logger.warn("获取当前登陆用户失败");
        } else {
            User u = (User) user;
            User newUser = new User();
            newUser.setId(user.getId());
            newUser.setTenant(u.getTenant());
            newUser.setName(user.getName());
            newUser.setTrueName(user.getTrueName());
            newUser.setType(user.getType());
            newUser.setRegisterTime(user.getRegisterTime());
            form.jsonResult(newUser);
        }
        return Page.JSPage;
    }

    /**
     * 初始化超级管理员root帐号
     * 
     * @param form
     * @param module
     * @return
     */
    public Page doInitRoot(WebForm form, Module module) {
        String skey = CommUtil.null2String(form.get("skey"));
        QueryObject qo = new QueryObject();
        qo.addQuery("obj.code", ShiroUtils.ROOT, "=");
        List<?> list = this.userService.getUserBy(qo).getResult();
        if (list == null || list.size() == 0 || "true".equals(skey)) {
            Tenant t = null;
            qo = new QueryObject();
            qo.addQuery("obj.code", ShiroUtils.ROOT, "=");
            List<Tenant> ts = this.tenantService.getTenantBy(qo).getResult();
            if (ts != null && ts.size() > 0) {
                t = ts.get(0);
            } else {
                t = new Tenant();
                t.setCode(ShiroUtils.ROOT);
                t.setTitle("顶级租户");
                t.setUrl("http://localhost");
                t.setStatus(1);
                this.tenantService.addTenant(t);
            }
            if (t.getId() != null) {
                User u = new User();
                u.setName(ShiroUtils.ROOT);
                u.setTrueName("超级管理员");
                u.setNickname("超级管理员");
                u.setCode(new Date().getTime() + "");
                u.setIsTenantAdmin(true);
                u.setTenant(t);
                this.userService.addUser(u);
            }
        }
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
        return new Page("/manage/login.html");
    }

    public void setuserService(IUserService userService) {
        this.userService = userService;
    }

    public void setTenantService(ITenantService tenantService) {
        this.tenantService = tenantService;
    }
}
